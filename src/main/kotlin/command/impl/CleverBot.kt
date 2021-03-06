package com.grosslicht.patricksbot.command.impl

import com.google.code.chatterbotapi.ChatterBotFactory
import com.google.code.chatterbotapi.ChatterBotSession
import com.google.code.chatterbotapi.ChatterBotType
import net.dv8tion.jda.core.entities.MessageChannel
import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.thread


class CleverBot {
    private val chatBotSession: ChatterBotSession =
            ChatterBotFactory().create(ChatterBotType.CLEVERBOT, System.getenv("CLEVERBOT_API_KEY")).createSession()

    fun ask(question: String, channel: MessageChannel) {
        thread {
            channel.sendTyping().queue()
            Thread.sleep(ThreadLocalRandom.current().nextLong(1000, 4000))
            channel.sendMessage(chatBotSession.think(question)).queue()
        }
    }
}
