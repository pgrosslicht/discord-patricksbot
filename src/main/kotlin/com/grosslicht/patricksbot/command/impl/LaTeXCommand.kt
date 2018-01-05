package com.grosslicht.patricksbot.command.impl

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpPost
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import mu.KLogging
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.net.URLEncoder

/**
 * Created by patrickgrosslicht on 25/02/17.
 */
class LaTeXCommand : CommandExecutor {
    companion object: KLogging()
    init {
        FuelManager.instance.basePath = System.getenv("MATHOID_URL") ?: "http://${System.getenv("MATHOID_SERVICE_HOST")}:${System.getenv("MATHOID_SERVICE_PORT")}"
    }
    fun getLanguage(msg: String) = msg.substring(msg.indexOf("```") + 3, msg.indexOf("\n"))


    fun render(channel: MessageChannel, code: String) {
        "/png".httpPost(listOf("q" to code)).response { _, _, result ->
            result.fold({ d ->
                channel.sendFile(d, "latex.png", null).queue()
            }, { err ->
                channel.sendMessage("Error while rendering LaTeX").queue()
                logger.error { err }
            })
        }
    }
    @Command(aliases = arrayOf(".latex"), description = "Renders LaTeX", async = true)
    fun handleCommand(message: Message) {
        if (message.contentDisplay.startsWith(".latex ```")) {
            val lang = getLanguage(message.contentDisplay)
            if (lang == "latex" || lang == "tex") {
                render(message.channel, message.contentDisplay.substring(message.contentDisplay.indexOf("```"), message.contentDisplay.lastIndexOf("```") + 3).replace(Regex("(^```(\\w+)?)|(```$)"), "").trim())
            }
        }
    }
}
