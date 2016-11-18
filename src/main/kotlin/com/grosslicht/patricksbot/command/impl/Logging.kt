package com.grosslicht.patricksbot.command.impl

import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

/**
 * Created by patrickgrosslicht on 16/11/16.
 */
class Logging : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent?) {
        super.onMessageReceived(event)
    }
}
