package com.grosslicht.patricksbot.extensions

import com.grosslicht.patricksbot.DBCache
import com.grosslicht.patricksbot.DataSource
import com.grosslicht.patricksbot.models.*
import io.requery.kotlin.Offset
import io.requery.kotlin.eq
import io.requery.query.Result
import net.dv8tion.jda.core.entities.TextChannel
import java.time.ZonedDateTime

/**
 * Created by patrickgrosslicht on 02/01/17.
 */

fun TextChannel.findOrCreate(): Channel {
    return DBCache.channelCache[this.id, {
        val data = DataSource.data
        var newChannel: Channel? = data.findByKey(Channel::class, this.id)
        if (newChannel == null) {
            newChannel = ChannelEntity()
            newChannel.setId(this.id)
            newChannel.setName(this.name)
            val guild: Guild = this.guild.findOrCreate()
            newChannel.setGuild(guild)
            data.insert(newChannel)
        } else {
            newChannel
        }
    }]
}

fun net.dv8tion.jda.core.entities.Guild.findOrCreate(): Guild {
    return DBCache.guildCache[this.id, {
        val data = DataSource.data
        var newGuild: Guild? = data.findByKey(Guild::class, this.id)
        if (newGuild == null) {
            newGuild = GuildEntity()
            newGuild.setId(this.id)
            newGuild.setName(this.name)
            val owner = this.owner.user.findOrCreate()
            newGuild.setOwner(owner)
            data.insert(newGuild)
        } else {
            newGuild
        }
    }]
}

fun net.dv8tion.jda.core.entities.User.findOrCreate(): User {
    return DBCache.userCache[this.id, {
        val data = DataSource.data
        var newUser: User? = data.findByKey(User::class, this.id)
        if (newUser == null) {
            newUser = UserEntity()
            newUser.setAvatarId(this.avatarId)
            newUser.setBot(this.isBot)
            newUser.setDiscriminator(this.discriminator)
            newUser.setId(this.id)
            newUser.setName(this.name)
            data.insert(newUser)
        } else {
            newUser
        }
    }]
}

fun net.dv8tion.jda.core.entities.User.findOrCreateMention(msg: Message): Mention {
    val data = DataSource.data
    val user = this.findOrCreate()
    val result: Offset<Result<MentionEntity>> = data.select(MentionEntity::class) where (Mention::user eq user and (Mention::message eq msg)) limit 1
    var newMention: Mention? = result.get().firstOrNull()
    if (newMention == null) {
        newMention = MentionEntity()
        newMention.setMessage(msg)
        newMention.setUser(user)
        return data.insert(newMention)
    } else {
        return newMention
    }
}

fun net.dv8tion.jda.core.entities.User.createIncident(offlineTime: ZonedDateTime, onlineTime: ZonedDateTime): Incident {
    val data = DataSource.data
    val user = this.findOrCreate()
    val newIncident = IncidentEntity()
    newIncident.setOfflineTime(offlineTime)
    newIncident.setOnlineTime(onlineTime)
    newIncident.setUser(user)
    return data.insert(newIncident)
}

fun net.dv8tion.jda.core.entities.Message.Attachment.findOrCreate(msg: Message): MessageAttachment {
    val data = DataSource.data
    var newAttachment: MessageAttachment? = data.findByKey(MessageAttachment::class, this.id)
    if (newAttachment == null) {
        newAttachment = MessageAttachmentEntity()
        newAttachment.setUrl(this.url)
        newAttachment.setId(this.id)
        newAttachment.setProxyUrl(this.proxyUrl)
        newAttachment.setFileName(this.fileName)
        newAttachment.setMessage(msg)
        return data.insert(newAttachment)
    } else {
        return newAttachment
    }
}

fun net.dv8tion.jda.core.entities.Message.create(): Message {
    val data = DataSource.data
    val user: User = this.author.findOrCreate()
    val channel: Channel = this.textChannel.findOrCreate()
    val msg = MessageEntity()
    msg.setUser(user)
    msg.setChannel(channel)
    msg.content = this.content
    msg.setId(this.id)
    msg.mentionsEverybody = this.mentionsEveryone()
    msg.setTime(this.creationTime)
    val res = data.insert(msg)
    if (this.attachments.isNotEmpty()) {
        this.attachments.forEach { attachments: net.dv8tion.jda.core.entities.Message.Attachment -> attachments.findOrCreate(msg) }
    }
    if (this.mentionedUsers.isNotEmpty()) {
        this.mentionedUsers.forEach { user: net.dv8tion.jda.core.entities.User ->
            val mention = user.findOrCreateMention(msg)
        }
    }
    return res
}

fun net.dv8tion.jda.core.entities.Message.upsert(): Message {
    val data = DataSource.data
    val user: User = this.author.findOrCreate()
    val channel: Channel = this.textChannel.findOrCreate()
    val msg = MessageEntity()
    msg.setUser(user)
    msg.setChannel(channel)
    msg.content = this.content
    msg.setId(this.id)
    msg.mentionsEverybody = this.mentionsEveryone()
    msg.setTime(this.creationTime)
    val res = data.upsert(msg)
    if (this.attachments.isNotEmpty()) {
        this.attachments.forEach { attachments: net.dv8tion.jda.core.entities.Message.Attachment -> attachments.findOrCreate(msg) }
    }
    if (this.mentionedUsers.isNotEmpty()) {
        this.mentionedUsers.forEach { user: net.dv8tion.jda.core.entities.User -> user.findOrCreateMention(msg) }
    }
    return res
}
