package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import mu.KLogging
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.TextChannel


class RmCommand : CommandExecutor {
    companion object: KLogging()

    @Command(aliases = arrayOf(".rm"), showInHelpPage = false, onlyOwner = true)
    fun remove(cmd: String, toDelete: String?, message: Message) {
        delete(toDelete?.toLong() ?: 1, { true }, message.textChannel, message)
    }

    @Command(aliases = arrayOf(".rmregex"), showInHelpPage = false, onlyOwner = true)
    fun removeRegex(cmd: String, regex: String, toDelete: String?, message: Message) {
        delete(toDelete?.toLong() ?: 1, { m : Message -> m.contentDisplay.matches(regex.toRegex()) }, message.textChannel, message)
    }

    @Command(aliases = arrayOf(".rmuser"), showInHelpPage = false, onlyOwner = true)
    fun removeUser(cmd: String, user: String, toDelete: String?, message: Message) {
        delete(toDelete?.toLong() ?: 1, { m : Message -> m.author.id == message.mentionedUsers.firstOrNull()?.id ?: -1 }, message.textChannel, message)
    }

    fun delete(toDelete: Long, filter: ((Message) -> Boolean), channel: TextChannel, message: Message) {
        if (toDelete < 1) {
            logger.error { "toDelete is less than 1!" }
            return
        }
        message.delete().queue {
            channel.history.retrievePast(toDelete.toInt()).queue { msgs: MutableList<Message> ->
                val filteredMessages = msgs.asSequence().filter(filter)
                when {
                    filteredMessages.count() < 2 -> filteredMessages.firstOrNull()?.delete()?.queue()
                    else -> channel.deleteMessages(filteredMessages.toList()).queue()
                }
            }
        }
    }
}
