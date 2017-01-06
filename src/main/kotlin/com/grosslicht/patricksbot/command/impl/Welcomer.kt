package com.grosslicht.patricksbot.command.impl

import mu.KLogging
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.time.Duration
import java.time.OffsetDateTime

/**
 * Created by patrickgrosslicht on 06/01/17.
 */
class Welcomer : ListenerAdapter() {
    companion object : KLogging()

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val channel = event.guild.getTextChannelsByName("welcome", true).getOrElse(1, {
            event.guild.getTextChannelsByName("general", true).first()
        })
        if (event.member.user.isBot) {
            channel.sendMessage("Oh look, another bot! Welcome ${event.member.nickname}! Now we have ${event.guild.members.filter { m -> m.user.isBot }.size} bots. Let me go ahead and give it the correct permissions.").queue {
                event.guild.controller.addRolesToMember(event.member, event.guild.getRolesByName("non-human", true)).queue()
            }
        } else {
            channel.sendMessage(MessageBuilder().appendFormat("Welcome %U%! We hope you have a good time here. Also, I'll give you your appropriate role now.", event.member.user).build()).queue {
                event.guild.controller.addRolesToMember(event.member, event.guild.getRolesByName("human", true)).queue()
            }
        }
    }

    override fun onGuildMemberLeave(event: GuildMemberLeaveEvent) {
        val channel = event.guild.getTextChannelsByName("welcome", true).getOrElse(1, {
            event.guild.getTextChannelsByName("general", true).first()
        })
        if (event.member.user.isBot) {
            channel.sendMessage("I guess that ${event.member.nickname} didn't work out. Now we're down to ${event.guild.members.filter { m -> m.user.isBot }.size} bots.").queue()
        } else {
            channel.sendMessage(MessageBuilder().appendFormat("%U% just fucking left. WTF? And he's only been here for ${Duration.between(event.member.joinDate, OffsetDateTime.now())}.", event.member.user).build()).queue()
        }
    }
}
