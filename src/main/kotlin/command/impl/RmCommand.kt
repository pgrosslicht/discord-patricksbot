package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import mu.KLogging
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.TextChannel


class RmCommand : CommandExecutor {
    companion object : KLogging()

    @Command(aliases = [".rm"], showInHelpPage = false, onlyOwner = true)
    fun remove(cmd: String, toDelete: String?, message: Message) {
        delete(toDelete?.toLong() ?: 1, { true }, message.textChannel, message)
    }

    @Command(aliases = [".rmregex"], showInHelpPage = false, onlyOwner = true)
    fun removeRegex(cmd: String, regex: String, toDelete: String?, message: Message) {
        delete(
                toDelete?.toLong() ?: 1,
                { m: Message -> m.contentDisplay.matches(regex.toRegex()) },
                message.textChannel,
                message
        )
    }

    @Command(aliases = [".rmuser"], showInHelpPage = false, onlyOwner = true)
    fun removeUser(cmd: String, user: String, toDelete: String?, message: Message) {
        delete(
                toDelete?.toLong() ?: 1,
                { m: Message -> m.author.id == message.mentionedUsers.firstOrNull()?.id ?: -1 },
                message.textChannel,
                message
        )
    }

    private fun delete(toDelete: Long, filter: ((Message) -> Boolean), channel: TextChannel, message: Message) {
        if (toDelete < 1) {
            logger.error { "toDelete is less than 1!" }
            return
        }
        channel.iterableHistory
                .takeAsync(toDelete.toInt())
                .thenApply { channel.purgeMessages(applyFilter(filter, it)) }
    }

    private fun applyFilter(filter: (Message) -> Boolean, messages: List<Message>): List<Message> = messages.filter(filter)
}
