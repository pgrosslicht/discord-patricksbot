package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.extensions.upsert
import mu.KLogging
import net.dv8tion.jda.core.entities.Message
import java.util.concurrent.ConcurrentLinkedQueue


class Scanner : Runnable {
    companion object : KLogging()

    val queue = ConcurrentLinkedQueue<Message>()
    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            try {
                while (queue.isNotEmpty()) {
                    try {
                        queue.poll().upsert()
                    } catch (e: io.requery.sql.StatementExecutionException) {
                        logger.error { e }
                    }
                }
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    fun addAll(e: Collection<Message>) {
        queue.addAll(e)
    }
}
