package com.grosslicht.patricksbot.command

import java.util.*

/**
 * Created by patrickgrosslicht on 15/11/16.
 */
class VersionCommand : CommandExecutor {
    @Command(aliases = arrayOf(".version", ".build"), description = "Shows what build the bot is running")
    fun onCommand(): String {
        return "I am PatricksBot v${getVersion()}"
    }

    fun getVersion(): String {
        val prop = Properties()
        val input = javaClass.classLoader.getResourceAsStream("build.properties")
        prop.load(input)
        return prop.getProperty("version")
    }
}
