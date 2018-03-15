package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.extensions.createIncident
import mu.KLogging
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.user.UserOnlineStatusUpdateEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

/**
 * Created by patrickgrosslicht on 15/11/16.
 */
class OfflineWarner : ListenerAdapter() {
    companion object : KLogging()
    val map = HashMap<String, ZonedDateTime>()
    val ownerMap : Map<String, String> = HashMap<String, String>(mapOf("219898908074049547" to "103255776218345472", //Magic 8-Ball
                                                "415712090809040897" to "103255776218345472", //Soring Markov
                                                "220006633365831680" to "103255776218345472", //{U}
                                                "415710764561727488" to "103255776218345472", //Nora
                                                "197553759151194112" to "103267161757204480", //Pancake
                                                "265702081267105793" to "103255776218345472", //MtG Cards
                                                "297501630847385601" to "103255776218345472" //DJ Pon3
            ))
    override fun onUserOnlineStatusUpdate(event: UserOnlineStatusUpdateEvent) {
        if (event.previousOnlineStatus == OnlineStatus.ONLINE && event.user.isBot) {
            logger.debug { "A bot that was online has gone offline, check which one" }
            val member = event.guild.getMember(event.user)
            if (member.onlineStatus == OnlineStatus.OFFLINE) {
                if (ownerMap.contains(event.user.id)) {
                    messageOwner(event, ownerMap[event.user.id]!!, event.user)
                }
            }
        } else if (event.previousOnlineStatus == OnlineStatus.OFFLINE && event.user.isBot) {
            logger.debug { "A bot that was offline has gone online, check which one" }
            val member = event.guild.getMember(event.user)
            if (member.onlineStatus == OnlineStatus.ONLINE) {
                if (ownerMap.contains(event.user.id)) {
                    messageOwnerOnline(event, ownerMap[event.user.id]!!, event.user)
                }
            }
        }
    }

    fun messageOwner(event: UserOnlineStatusUpdateEvent, owner: String, affectedBot: User) {
        logger.debug { "${affectedBot.name} has gone offline at ${LocalDateTime.now()}" }
        map.put(affectedBot.id, ZonedDateTime.now(ZoneId.of("Z")))
        val message = MessageBuilder().appendFormat("Hey %s, your bot %s has just gone offline!", event.guild.getMemberById(owner).user, affectedBot)
        event.guild.getTextChannelById("138404620128092160").sendMessage(message.build()).queue()
    }

    fun messageOwnerOnline(event: UserOnlineStatusUpdateEvent, owner: String, affectedBot: User) {
        logger.debug { "${affectedBot.name} has come back online at ${LocalDateTime.now()}" }
        val date = map.getOrElse(affectedBot.id, { logger.debug { "Could not find time when bot went offline" }; return })
        val duration = Duration.between(date, ZonedDateTime.now())
        affectedBot.createIncident(date, ZonedDateTime.now(ZoneId.of("Z")))
        val message = MessageBuilder().appendFormat("Well finally… That took %s for %s to come back online.", duration.toString(), affectedBot.name)
        event.guild.getTextChannelById("138404620128092160").sendMessage(message.build()).queue()
    }
}
