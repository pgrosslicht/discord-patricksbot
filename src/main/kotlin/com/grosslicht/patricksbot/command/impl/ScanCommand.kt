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

/**
 * Created by patrickgrosslicht on 02/01/17.
 */
class ScanCommand : CommandExecutor {
    companion object: KLogging()
    var i = 0
    var stopwatch = Stopwatch.createUnstarted()
    var scanners: MutableMap<String, Scanner> = HashMap()
    var scannerThreads: MutableMap<String, Thread> = HashMap()

    @Command(aliases = arrayOf(".scan"), showInHelpPage = false, onlyOwner = true, async = true)
    fun scan(channel: TextChannel, jda: JDA) {
        i = 0
        logger.debug { "Scan started for ${channel.id}" }
        val scanner = Scanner()
        val scannerThread = Thread(scanner)
        scannerThread.start()
        scanners.put(channel.id, scanner)
        scannerThreads.put(channel.id, scannerThread)
        stopwatch = Stopwatch.createStarted()
        walkChannelHistory(channel.id, channel.history)
    }

    fun walkChannelHistory(channelId: String, history: MessageHistory) {
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
