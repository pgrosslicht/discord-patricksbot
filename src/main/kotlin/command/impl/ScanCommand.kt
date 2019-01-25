package com.grosslicht.patricksbot.command.impl

import com.google.common.base.Stopwatch
import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import mu.KLogging
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.MessageHistory
import net.dv8tion.jda.core.entities.TextChannel
import java.util.*
import java.util.concurrent.TimeUnit


class ScanCommand : CommandExecutor {
    companion object : KLogging()

    private var i = 0
    private var stopwatch = Stopwatch.createUnstarted()
    private var scanners: MutableMap<String, Scanner> = HashMap()
    private var scannerThreads: MutableMap<String, Thread> = HashMap()

    @Command(aliases = [".scan"], showInHelpPage = false, onlyOwner = true, async = true)
    fun scan(channel: TextChannel, jda: JDA) {
        i = 0
        logger.debug { "Scan started for ${channel.id}" }
        val scanner = Scanner()
        val scannerThread = Thread(scanner)
        scannerThread.start()
        scanners[channel.id] = scanner
        scannerThreads[channel.id] = scannerThread
        stopwatch = Stopwatch.createStarted()
        walkChannelHistory(channel.id, channel.history)
        channel.iterableHistory
    }

    private fun walkChannelHistory(channelId: String, history: MessageHistory) {
        //TODO: rewrite with iterableHistory and bulk upserts
        history.retrievePast(100).queue { list ->
            i += list.size
            scanners[channelId]?.addAll(list)
            if (list.size == 100) {
                logger.debug { "Getting another 100! Already got $i" }
                walkChannelHistory(channelId, history)
            } else {
                stopwatch.stop()
                logger.debug { "Finished scan for $channelId. Scanned $i messages in ${stopwatch.elapsed(TimeUnit.MILLISECONDS)} ms." }
                scannerThreads[channelId]?.interrupt()
                scanners.remove(channelId)
                scannerThreads.remove(channelId)
            }
        }
    }
}
