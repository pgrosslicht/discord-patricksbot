package com.grosslicht.patricksbot

import com.grosslicht.patricksbot.command.JDACommandHandler
import com.grosslicht.patricksbot.command.impl.*
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder


fun main(args: Array<String>) {
    val builder = JDABuilder(AccountType.BOT)
    val token = System.getenv("DISCORD_API_TOKEN")
    builder.setToken(token)
    val jda = builder.build().awaitReady()
    jda.addEventListener(Logging())
    jda.addEventListener(OfflineWarner())
    jda.addEventListener(Welcomer())
    jda.addEventListener(CleverBotListener())
    val cmdHandler = JDACommandHandler(jda)
    cmdHandler.registerCommand(ScanCommand())
    cmdHandler.registerCommand(InfoCommand())
    cmdHandler.registerCommand(PingCommand())
    //cmdHandler.registerCommand(CodeCommand())
    cmdHandler.registerCommand(LaTeXCommand())
    cmdHandler.registerCommand(RmCommand())
    cmdHandler.registerCommand(StatusCommand())
    cmdHandler.registerCommand(Insulter())
    cmdHandler.registerCommand(WordCloudCommand())
    cmdHandler.registerCommand(HelpCommand(cmdHandler))
}
