package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.DataSource.data
import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import com.grosslicht.patricksbot.models.MessageEntity
import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.bg.RectangleBackground
import com.kennycason.kumo.font.FontWeight
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.nlp.FrequencyAnalyzer
import com.kennycason.kumo.nlp.filter.UrlFilter
import com.kennycason.kumo.palette.ColorPalette
import mu.KLogging
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageChannel
import java.awt.Color
import java.awt.Dimension
import java.io.ByteArrayOutputStream
import java.time.ZonedDateTime


/**
 * Created by patrickgrosslicht on 08/01/17.
 */
class WordCloudCommand : CommandExecutor {
    companion object : KLogging()

    fun generateWordCloud(channel: MessageChannel) {
        val words: List<String> = data.select(MessageEntity.ID, MessageEntity.CONTENT).from(MessageEntity::class).where(MessageEntity.CHANNEL_ID.eq(channel.id)).get().map { m -> m.content }
        val frequencyAnalyzer = FrequencyAnalyzer()
        frequencyAnalyzer.addFilter(UrlFilter())
        val wordFrequencies: List<WordFrequency> = frequencyAnalyzer.load(words)
        val dimension: Dimension = Dimension(1024, 1024)
        val wordCloud = WordCloud(dimension, CollisionMode.RECTANGLE)
        wordCloud.setPadding(0)
        wordCloud.setKumoFont(KumoFont("LICENSE PLATE", FontWeight.BOLD))
        wordCloud.setBackground(RectangleBackground(dimension))
        wordCloud.setColorPalette(ColorPalette(Color(0xD5CFFA), Color(0xBBB1FA), Color(0x9A8CF5),
                Color(0x806EF5)))
        wordCloud.setFontScalar(LinearFontScalar(10, 40))
        wordCloud.build(wordFrequencies)
        val byteArrayOutputStream = ByteArrayOutputStream()
        wordCloud.writeToStreamAsPNG(byteArrayOutputStream)
        channel.sendFile(byteArrayOutputStream.toByteArray(), "wordcloud-${channel.id}-${ZonedDateTime.now().toEpochSecond()}.png", null).queue()
    }

    @Command(aliases = arrayOf(".wordcloud"), usage = ".wordcloud", description = "Creates a word cloud of the current channel", async = true)
    fun wordCloud(message: Message) {
        generateWordCloud(message.channel)
    }
}