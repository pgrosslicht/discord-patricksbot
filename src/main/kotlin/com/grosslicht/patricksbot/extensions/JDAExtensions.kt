package com.grosslicht.patricksbot.extensions

import com.grosslicht.patricksbot.DataSource
import com.grosslicht.patricksbot.models.*
import net.dv8tion.jda.core.entities.TextChannel

/**
 * Created by patrickgrosslicht on 02/01/17.
 */

fun TextChannel.findOrCreate(): Channel {
    val data = DataSource.data
    var newChannel: Channel? = data.findByKey(Channel::class, this.id)
    if (newChannel == null) {
        newChannel = ChannelEntity()
        newChannel.setId(this.id)
        newChannel.setName(this.name)
        val guild: Guild = this.guild.findOrCreate()
        newChannel.setGuild(guild)
        return data.insert(newChannel)
    } else {
        return newChannel
    }
}

fun net.dv8tion.jda.core.entities.Guild.findOrCreate(): Guild {
    val data = DataSource.data
    var newGuild: Guild? = data.findByKey(Guild::class, this.id)
    if (newGuild == null) {
        newGuild = GuildEntity()
        newGuild.setId(this.id)
        newGuild.setName(this.name)
        val owner = this.owner.user.findOrCreate()
        newGuild.setOwner(owner)
        return data.insert(newGuild)
    } else {
        return newGuild
    }
}

fun net.dv8tion.jda.core.entities.User.findOrCreate(): User {
    val data = DataSource.data
    var newUser: User? = data.findByKey(User::class, this.id)
    if (newUser == null) {
        newUser = UserEntity()
        newUser.setAvatarId(this.avatarId)
        newUser.setBot(this.isBot)
        newUser.setDiscriminator(this.discriminator)
        newUser.setId(this.id)
        newUser.setName(this.name)
        return data.insert(newUser)
    } else {
        return newUser
    }
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
    msg.setTime(this.creationTime)
    val res = data.insert(msg)
    if (this.attachments.isNotEmpty()) {
        this.attachments.forEach { attachments: net.dv8tion.jda.core.entities.Message.Attachment -> attachments.findOrCreate(msg) }
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
    msg.setTime(this.creationTime)
    return data.upsert(msg)
}
