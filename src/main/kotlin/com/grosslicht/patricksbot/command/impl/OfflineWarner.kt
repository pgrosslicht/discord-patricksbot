package com.grosslicht.patricksbot.command.impl

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
    val map = HashMap<String, LocalDateTime>()
    override fun onUserOnlineStatusUpdate(event: UserOnlineStatusUpdateEvent?) {
        if (event == null)
            return
        if (event.previousOnlineStatus == OnlineStatus.ONLINE && event.user.isBot) {
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
        map.put(affectedBot.id, LocalDateTime.now())
        val message = MessageBuilder().appendFormat("Hey %U%, your bot %U% has just gone offline!", event.guild.getMemberById(owner).user, affectedBot)
        event.guild.getTextChannelById("138404620128092160").sendMessage(message.build()).queue()
    }

    fun messageOwnerOnline(event: UserOnlineStatusUpdateEvent, owner: String, affectedBot: User) {
        val date = map.getOrElse(affectedBot.id) { return }
        val duration = Duration.between(date, LocalDateTime.now())
        val message = MessageBuilder().appendFormat("Well finally… That took %s for %U% to come back online.", duration.toString(), affectedBot)
        event.guild.getTextChannelById("138404620128092160").sendMessage(message.build()).queue()
    }
}
