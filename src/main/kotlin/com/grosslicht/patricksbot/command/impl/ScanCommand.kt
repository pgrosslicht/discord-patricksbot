package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import mu.KLogging
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.MessageHistory

/**
 * Created by patrickgrosslicht on 02/01/17.
 */
class ScanCommand : CommandExecutor {
    companion object: KLogging()
    var scanner: Scanner? = null

    @Command(aliases = arrayOf(".scan"), showInHelpPage = false, onlyOwner = true)
    fun scan(cmd: String, channel: String, jda: JDA) {
        if (scanner == null) {
            scanner = Scanner()
            scanner?.run()
        }
        walkChannelHistory(jda.getTextChannelById(channel).history)
    }

    fun walkChannelHistory(history: MessageHistory) {
        history.retrievePast(100).queue { list ->
            scanner?.addAll(list)
            if (list.size == 100) {
                logger.debug { "Getting another 100!" }
                walkChannelHistory(history)
            } else {
                logger.debug { "Finished scan" }
                scanner?.stop()
                scanner = null
            }
        }
    }
}