package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import java.lang.management.ManagementFactory
import java.util.*
import java.util.concurrent.TimeUnit


class InfoCommand : CommandExecutor {
    private val version: String by lazy {
        val prop = Properties()
        val input = javaClass.classLoader.getResourceAsStream("build.properties")
        prop.load(input)
        prop.getProperty("version")
    }

    @Command(aliases = [".version", ".build"], description = "Shows what build the bot is running.")
    fun onVersionCommand(): String {
        return "I am PatricksBot v$version"
    }

    @Command(aliases = [".uptime", ".up"], description = "Shows how long the bot has been online for.")
    fun onUptimeCommand(): String {
        val millis = ManagementFactory.getRuntimeMXBean().uptime
        return "I have been up for ${String.format(
                "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
        )}."
    }
}
