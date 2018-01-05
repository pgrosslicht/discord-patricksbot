package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.DataSource
import com.grosslicht.patricksbot.extensions.create
import com.grosslicht.patricksbot.models.Message
import com.grosslicht.patricksbot.models.MessageRevisionEntity
import mu.KLogging
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.events.message.MessageDeleteEvent
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.events.message.MessageUpdateEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter


/**
 * Created by patrickgrosslicht on 16/11/16.
 */
class Logging : ListenerAdapter() {
    companion object : KLogging()
    val data = DataSource.data

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.channelType == ChannelType.TEXT) {
            event.message.create()
        }
        logger.debug { "Received message #${event.message.id} '${event.message.contentDisplay}' from ${event.author.name}" }
    }

    override fun onMessageDelete(event: MessageDeleteEvent) {
        if (event.channelType == ChannelType.TEXT) {
            data.invoke {
                var message: Message? = findByKey(Message::class, event.messageId)
                if (message != null) {
                    message.isDeleted = true
                    update(message)
                }
            }
        }
        logger.debug { "${event.messageId} has been deleted" }
    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {
        if (event.channelType == ChannelType.TEXT) {
            data.invoke {
                var message: Message? = findByKey(Message::class, event.message.id)
                if (message != null) {
                    val rev = MessageRevisionEntity()
                    rev.setMessage(message)
                    rev.setTime(message.time)
                    rev.setContent(message.content)
                    insert(rev)
                    message.isEdited = true
                    message.content = event.message.contentDisplay
                    update(message)
                }
            }
        }
        logger.debug { "Message updated: '${event.message.contentDisplay}' by ${event.author.name}" }
    }
}




