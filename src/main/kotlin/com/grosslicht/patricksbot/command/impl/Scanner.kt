package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.extensions.upsert
import mu.KLogging
import net.dv8tion.jda.core.entities.Message
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by patrickgrosslicht on 03/01/17.
 */
class Scanner : Runnable {
    companion object: KLogging()
    val queue = ConcurrentLinkedQueue<Message>()
    var running = true
    override fun run() {
        while (running) {
            while (queue.isNotEmpty()) {
                try {
                    queue.poll().upsert()
                } catch (e: Exception) {
                    logger.error { e }
                }
            }
        }
    }

    fun addAll(e: Collection<Message>) {
        queue.addAll(e)
    }

    fun stop() {
        running = false
    }
}