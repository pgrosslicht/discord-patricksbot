package com.grosslicht.patricksbot.command.impl

import mu.KLogging
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.user.UserOnlineStatusUpdateEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

/**
 * Created by patrickgrosslicht on 15/11/16.
 */
class OfflineWarner : ListenerAdapter() {
    companion object : KLogging()
    val map = HashMap<String, LocalDateTime>()
    override fun onUserOnlineStatusUpdate(event: UserOnlineStatusUpdateEvent?) {
        if (event == null)
            return
        if (event.previousOnlineStatus == OnlineStatus.ONLINE && event.user.isBot) {
            logger.debug { "A bot that was online has gone offline, check which one" }
            val member = event.guild.getMember(event.user)
            if (member.onlineStatus == OnlineStatus.OFFLINE) {
                when (event.user.id) {
                    "219898908074049547" -> messageOwner(event, "103255776218345472", event.user) //Magic 8-Ball
                    "196809469458382848" -> messageOwner(event, "103255776218345472", event.user) //Sorin Markov
                    "220006633365831680" -> messageOwner(event, "103255776218345472", event.user) //{U}
                    "196059744991969280" -> messageOwner(event, "103255776218345472", event.user) //Nora
                    "197553759151194112" -> messageOwner(event, "103267161757204480", event.user) //Pancake
                }
            }
        } else if (event.previousOnlineStatus == OnlineStatus.OFFLINE && event.user.isBot) {
            logger.debug { "A bot that was offline has gone online, check which one" }
            val member = event.guild.getMember(event.user)
            if (member.onlineStatus == OnlineStatus.ONLINE) {
                when (event.user.id) {
                    "219898908074049547" -> messageOwnerOnline(event, "103255776218345472", event.user) //Magic 8-Ball
                    "196809469458382848" -> messageOwnerOnline(event, "103255776218345472", event.user) //Sorin Markov
                    "220006633365831680" -> messageOwnerOnline(event, "103255776218345472", event.user) //{U}
                    "196059744991969280" -> messageOwnerOnline(event, "103255776218345472", event.user) //Nora
                    "197553759151194112" -> messageOwnerOnline(event, "103267161757204480", event.user) //Pancake
                }
            }
        }
    }

    fun messageOwner(event: UserOnlineStatusUpdateEvent, owner: String, affectedBot: User) {
        logger.debug { "${affectedBot.name} has gone offline at ${LocalDateTime.now()}" }
        map.put(affectedBot.id, LocalDateTime.now())
        val message = MessageBuilder().appendFormat("Hey %U%, your bot %U% has just gone offline!", event.guild.getMemberById(owner).user, affectedBot)
        event.guild.getTextChannelById("138404620128092160").sendMessage(message.build()).queue()
    }

    fun messageOwnerOnline(event: UserOnlineStatusUpdateEvent, owner: String, affectedBot: User) {
        logger.debug { "${affectedBot.name} has come back online at ${LocalDateTime.now()}" }
        val date = map.getOrElse(affectedBot.id, { logger.debug { "Could not find time when bot went offline" }; return })
        val duration = Duration.between(date, LocalDateTime.now())
        val message = MessageBuilder().appendFormat("Well finally… That took %s for %U% to come back online.", duration.toString(), affectedBot)
        event.guild.getTextChannelById("138404620128092160").sendMessage(message.build()).queue()
    }
}
