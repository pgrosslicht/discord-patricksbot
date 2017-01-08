package com.grosslicht.patricksbot.command.impl

import com.google.code.chatterbotapi.ChatterBotFactory
import com.google.code.chatterbotapi.ChatterBotSession
import com.google.code.chatterbotapi.ChatterBotType
import mu.KLogging
import net.dv8tion.jda.core.entities.MessageChannel
import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.thread

/**
 * Created by patrickgrosslicht on 08/01/17.
 */
class CleverBot {
    val chatBotSession: ChatterBotSession = ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession()

    fun ask(question: String, channel: MessageChannel) {
        thread {
            channel.sendTyping().queue()
            Thread.sleep(ThreadLocalRandom.current().nextLong(1000, 4000))
            channel.sendMessage(chatBotSession.think(question)).queue()
        }
    }
}
