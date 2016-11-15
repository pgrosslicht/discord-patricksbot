package com.grosslicht.patricksbot.command

/**
 * Created by patrickgrosslicht on 12/11/16.
 */
class TestCommand: CommandExecutor {

    @Command(aliases = arrayOf(".ping"), description = "Pong!")
    fun onCommand(): String {
        return "Pong!"
    }
}
