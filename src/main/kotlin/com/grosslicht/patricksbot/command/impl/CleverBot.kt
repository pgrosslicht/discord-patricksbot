package com.grosslicht.patricksbot.command.impl

import com.github.kittinunf.fuel.httpPost
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import mu.KLogging
import net.dv8tion.jda.core.entities.MessageChannel
import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.thread

/**
 * Created by patrickgrosslicht on 08/01/17.
 */
class CleverBot {
    companion object : KLogging()
    data class CreateRequest(val user: String, val key: String)
    data class CreateResponse(val status: String, val nick: String)
    data class AskRequest(val user: String, val key: String, val nick: String, val text: String)
    data class AskResponse(val status: String, val response: String)

    var nick = ""

    init {
        "https://cleverbot.io/1.0/create".httpPost().body(Gson().toJson(CreateRequest(System.getenv("CLEVERBOT_API_USER"), System.getenv("CLEVERBOT_API_KEY"))))
                .header(mapOf("Content-Type" to "application/json", "Accept" to "application/json", "User-Agent" to "PatricksBot/CleverBot"))
                .responseString { request, response, result ->
                    result.fold({ d ->
                        val createResponse = Gson().fromJson<CreateResponse>(d)
                        if (createResponse.status == "success") {
                            nick = createResponse.nick
                        } else {
                            logger.error { createResponse.status }
                        }
                    }, { err ->
                        logger.error { err }
                    })
                }
    }

    fun ask(question: String, channel: MessageChannel) {
        thread {
            channel.sendTyping().queue()
            Thread.sleep(ThreadLocalRandom.current().nextLong(1000, 5000))
            "https://cleverbot.io/1.0/ask".httpPost().body(Gson().toJson(AskRequest(System.getenv("CLEVERBOT_API_USER"), System.getenv("CLEVERBOT_API_KEY"), nick, question)))
                    .header(mapOf("Content-Type" to "application/json", "Accept" to "application/json", "User-Agent" to "PatricksBot/CleverBot"))
                    .responseString { request, response, result ->
                        result.fold({ d ->
                            val createResponse = Gson().fromJson<AskResponse>(d)
                            if (createResponse.status == "success") {
                                channel.sendMessage(createResponse.response).queue { }
                            } else {
                                logger.error { createResponse.status }
                            }
                        }, { err ->
                            logger.error { err }
                        })
                    }
        }
    }
}
