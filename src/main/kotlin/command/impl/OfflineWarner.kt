package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.extensions.createIncident
import mu.KLogging
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*


class OfflineWarner : ListenerAdapter() {
    companion object : KLogging()

    private val map = HashMap<String, ZonedDateTime>()
    private val ownerMap: Map<String, String> = HashMap<String, String>(
            mapOf(
                    "219898908074049547" to "103255776218345472", //Magic 8-Ball
                    "415712090809040897" to "103255776218345472", //Soring Markov
                    "220006633365831680" to "103255776218345472", //{U}
                    "415710764561727488" to "103255776218345472", //Nora
                    "197553759151194112" to "103267161757204480", //Pancake
                    "265702081267105793" to "103255776218345472", //MtG Cards
                    "297501630847385601" to "103255776218345472" //DJ Pon3
            )
    )

    override fun onUserUpdateOnlineStatus(event: UserUpdateOnlineStatusEvent) {
        if (event.oldOnlineStatus == OnlineStatus.ONLINE && event.user.isBot) {
            logger.debug { "A bot that was online has gone offline, check which one" }
            if (event.newOnlineStatus == OnlineStatus.OFFLINE) {
                if (ownerMap.contains(event.user.id)) {
                    messageOwner(event, ownerMap[event.user.id]!!, event.user)
                }
            }
        } else if (event.oldOnlineStatus == OnlineStatus.OFFLINE && event.user.isBot) {
            logger.debug { "A bot that was offline has gone online, check which one" }
            if (event.newOnlineStatus == OnlineStatus.ONLINE) {
                if (ownerMap.contains(event.user.id)) {
                    messageOwnerOnline(event, ownerMap[event.user.id]!!, event.user)
                }
            }
        }
    }

    private fun messageOwner(event: UserUpdateOnlineStatusEvent, owner: String, affectedBot: User) {
        logger.debug { "${affectedBot.name} has gone offline at ${LocalDateTime.now()}" }
        map.put(affectedBot.id, ZonedDateTime.now(ZoneId.of("Z")))
        val message = MessageBuilder().appendFormat(
                "Hey %s, your bot %s has just gone offline!",
                event.guild.getMemberById(owner).user,
                affectedBot
        )
        event.guild.getTextChannelById("449196251720712193").sendMessage(message.build()).queue()
    }

    private fun messageOwnerOnline(event: UserUpdateOnlineStatusEvent, owner: String, affectedBot: User) {
        logger.debug { "${affectedBot.name} has come back online at ${LocalDateTime.now()}" }
        val date =
                map.getOrElse(affectedBot.id) { logger.debug { "Could not find time when bot went offline" }; return }
        val duration = Duration.between(date, ZonedDateTime.now())
        affectedBot.createIncident(date, ZonedDateTime.now(ZoneId.of("Z")))
        val message = MessageBuilder().appendFormat(
                "Well finally… That took %s for %s to come back online.",
                duration.toString(),
                affectedBot.name
        )
        event.guild.getTextChannelById("449196251720712193").sendMessage(message.build()).queue()
    }
}
