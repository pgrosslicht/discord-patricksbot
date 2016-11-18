package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor

/**
 * Created by patrickgrosslicht on 12/11/16.
 */
class TestCommand: CommandExecutor {

    @Command(aliases = arrayOf(".ping"), description = "Pong!")
    fun onCommand(): String {
        return "Pong!"
    }
}
