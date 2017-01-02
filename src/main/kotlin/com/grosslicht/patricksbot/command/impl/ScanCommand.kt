package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import com.grosslicht.patricksbot.extensions.upsert
import mu.KLogging
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.MessageHistory
import net.dv8tion.jda.core.entities.Message

/**
 * Created by patrickgrosslicht on 02/01/17.
 */
class ScanCommand : CommandExecutor {
    companion object: KLogging()

    @Command(aliases = arrayOf(".scan"), showInHelpPage = false, onlyOwner = true)
    fun scan(cmd: String, channel: String, jda: JDA) {
        walkChannelHistory(jda.getTextChannelById(channel).history)
    }

    fun walkChannelHistory(history: MessageHistory) {
        history.retrievePast(100).queue { list ->
            list.forEach { msg: Message -> msg.upsert() }
            if (list.size == 100) {
                logger.debug { "Getting another 100!" }
                walkChannelHistory(history)
            }
        }
    }
}