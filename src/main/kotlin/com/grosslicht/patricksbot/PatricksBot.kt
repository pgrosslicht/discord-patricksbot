package com.grosslicht.patricksbot

import com.grosslicht.patricksbot.command.*
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder

/**
 * Created by patrickgrosslicht on 13/10/16.
 */

fun main(args: Array<String>) {
    val builder = JDABuilder(AccountType.BOT)
    val token = System.getenv("DISCORD_API_TOKEN")
    builder.setToken(token)
    val jda = builder.buildBlocking()
    jda.addEventListener(OfflineWarner())
    val cmdHandler = JDACommandHandler(jda)
    cmdHandler.registerCommand(UptimeCommand())
    cmdHandler.registerCommand(VersionCommand())
    cmdHandler.registerCommand(TestCommand())
    cmdHandler.registerCommand(HelpCommand(cmdHandler))
}
