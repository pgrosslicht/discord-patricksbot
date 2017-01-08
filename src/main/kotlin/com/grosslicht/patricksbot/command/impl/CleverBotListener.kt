package com.grosslicht.patricksbot.command.impl

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

/**
 * Created by patrickgrosslicht on 08/01/17.
 */
class CleverBotListener : ListenerAdapter() {
    val cleverBot = CleverBot()
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (event.channel.name.toLowerCase() == System.getenv("CLEVERBOT_CHANNEL")?.toLowerCase() ?: "bottalk") {
            if (event.message.author != event.jda.selfUser) {
                cleverBot.ask(event.message.content, event.channel)
            }
        }
    }
}
