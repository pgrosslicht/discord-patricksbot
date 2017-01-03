package com.grosslicht.patricksbot

import com.grosslicht.patricksbot.command.JDACommandHandler
import com.grosslicht.patricksbot.command.impl.*
import com.grosslicht.patricksbot.models.Models
import io.requery.sql.SchemaModifier
import io.requery.sql.TableCreationMode
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder


/**
 * Created by patrickgrosslicht on 13/10/16.
 */
//TODO: Logging
fun main(args: Array<String>) {
    val builder = JDABuilder(AccountType.BOT)
    val token = System.getenv("DISCORD_API_TOKEN")
    builder.setToken(token)
    val jda = builder.buildBlocking()
    jda.addEventListener(Logging())
    jda.addEventListener(OfflineWarner())
    val cmdHandler = JDACommandHandler(jda)
    cmdHandler.registerCommand(InfoCommand())
    cmdHandler.registerCommand(PingCommand())
    //cmdHandler.registerCommand(CodeCommand())
    cmdHandler.registerCommand(RmCommand())
    cmdHandler.registerCommand(StatusCommand())
    cmdHandler.registerCommand(Insulter())
    cmdHandler.registerCommand(ScanCommand())
    cmdHandler.registerCommand(HelpCommand(cmdHandler))

    val dataSource = DataSource.dataSource
    val model = Models.DEFAULT
    SchemaModifier(dataSource, model).createTables(TableCreationMode.DROP_CREATE)

}
