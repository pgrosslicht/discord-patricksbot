package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Message

/**
 * Created by patrickgrosslicht on 14/12/16.
 */
class StatusCommand : CommandExecutor {
    @Command(aliases = arrayOf(".status"), onlyOwner = true, showInHelpPage = false)
    fun onStatusCommand(message: Message, jda: JDA) {
        when {
            message.contentDisplay.contains("invisible") -> jda.presence.status = OnlineStatus.INVISIBLE
            message.contentDisplay.contains("offline") -> jda.presence.status = OnlineStatus.INVISIBLE
            message.contentDisplay.contains("dnd") -> jda.presence.status = OnlineStatus.DO_NOT_DISTURB
            message.contentDisplay.contains("afk") -> jda.presence.status = OnlineStatus.IDLE
            else -> jda.presence.status = OnlineStatus.ONLINE
        }
    }
}