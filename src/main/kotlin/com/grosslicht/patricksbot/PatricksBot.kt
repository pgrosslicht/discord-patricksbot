package com.grosslicht.patricksbot

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
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
    val token = "MjM2NTYxODE1MjcyODE2NjQx.CuwdvQ.R3WXyJeksiRGXR89JtBLZYaNvnY"
    builder.setToken(token)
    val jda = builder.buildBlocking()
    jda.addEventListener(Logging())
    jda.addEventListener(OfflineWarner())
    val cmdHandler = JDACommandHandler(jda)
    cmdHandler.registerCommand(InfoCommand())
    cmdHandler.registerCommand(PingCommand())
    cmdHandler.registerCommand(CodeCommand())
    cmdHandler.registerCommand(HelpCommand(cmdHandler))
    if (false) {
        val firebaseJson = object : Any() {}.javaClass.classLoader.getResourceAsStream("firebase.json")
        val auth = mapOf<String, Any>(Pair("uid", "patricksbot"))
        val options = FirebaseOptions.Builder().setServiceAccount(firebaseJson).setDatabaseUrl("https://mtg-emblems.firebaseio.com").setDatabaseAuthVariableOverride(auth).build()
        FirebaseApp.initializeApp(options)
        cmdHandler.registerCommand(EmblemCommand())
    }
}
