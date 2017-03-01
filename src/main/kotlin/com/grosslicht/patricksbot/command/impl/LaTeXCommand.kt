package com.grosslicht.patricksbot.command.impl

import com.github.kittinunf.fuel.core.FuelManager
import com.google.gson.JsonParser
import mu.KLogging
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

/**
 * Created by patrickgrosslicht on 25/02/17.
 */
class LaTeXCommand : ListenerAdapter() {
    companion object: KLogging()
    init {
        FuelManager.instance.basePath = System.getenv("MATHJAX_HOST") ?: "http://${System.getenv("MATHJAX_API_SERVICE_HOST")}:${System.getenv("MATHJAX_API_SERVICE_PORT")}"
    }
    val parse = JsonParser()
    fun getLanguage(msg: String) = msg.substring(msg.indexOf("```") + 3, msg.indexOf("\n"))

    override fun onMessageReceived(event: MessageReceivedEvent) {
    }
}
