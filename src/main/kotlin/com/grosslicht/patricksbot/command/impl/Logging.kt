package com.grosslicht.patricksbot.command.impl

import mu.KLogging
import net.dv8tion.jda.core.events.message.MessageDeleteEvent
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.events.message.MessageUpdateEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

/**
 * Created by patrickgrosslicht on 16/11/16.
 */
class Logging : ListenerAdapter() {
    companion object : KLogging()
    override fun onMessageReceived(event: MessageReceivedEvent) {
        logger.debug { "Received message '${event.message.content}' from ${event.author.name}" }
    }

    override fun onMessageDelete(event: MessageDeleteEvent) {
        logger.debug { "${event.messageId} has been deleted" }
    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {
        logger.debug { "Message updated: '${event.message.content}' by ${event.author.name}" }
    }
}
