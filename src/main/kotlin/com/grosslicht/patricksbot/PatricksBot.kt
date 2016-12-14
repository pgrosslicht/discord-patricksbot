package com.grosslicht.patricksbot

import com.grosslicht.patricksbot.command.JDACommandHandler
import com.grosslicht.patricksbot.command.impl.*
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder

/**
 * Created by patrickgrosslicht on 13/10/16.
 */
//TODO: Logging
fun main(args: Array<String>) {
    val builder = JDABuilder(AccountType.BOT)
    //val token = System.getenv("DISCORD_API_TOKEN")
    val token = "MjE2MTc5NDk4NjgzNTk2ODAy.CygW9w.QuAjY2tqDCjt1gzIUiEbk5Z6EnQ"
    builder.setToken(token)
    val jda = builder.buildBlocking()
    jda.addEventListener(Logging())
    jda.addEventListener(OfflineWarner())
    val cmdHandler = JDACommandHandler(jda)
    cmdHandler.registerCommand(InfoCommand())
    cmdHandler.registerCommand(PingCommand())
    //cmdHandler.registerCommand(CodeCommand())
    cmdHandler.registerCommand(RmCommand())
    cmdHandler.registerCommand(Insulter())
    cmdHandler.registerCommand(HelpCommand(cmdHandler))
}
