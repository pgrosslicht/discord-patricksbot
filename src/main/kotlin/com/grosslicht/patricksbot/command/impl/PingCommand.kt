package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.TextChannel
import java.time.temporal.ChronoUnit



/**
 * Created by patrickgrosslicht on 12/11/16.
 */
class PingCommand : CommandExecutor {

    @Command(aliases = arrayOf(".ping"), description = "Pong!")
    fun onCommand(message: Message, channel: TextChannel) {
        channel.sendMessage("Pinging...").queue({ m ->
            m.editMessage("Pong: " + message.creationTime.until(m.creationTime, ChronoUnit.MILLIS) + "ms").queue()
        })
    }
}
