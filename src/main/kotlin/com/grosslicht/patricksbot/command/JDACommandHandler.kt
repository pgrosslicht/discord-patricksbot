package com.grosslicht.patricksbot.command

import mu.KLogging
import net.dv8tion.jda.client.entities.Group
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import java.lang.reflect.InvocationTargetException
import net.dv8tion.jda.core.hooks.ListenerAdapter



/**
 * Created by patrickgrosslicht on 03/11/16.
 */
class JDACommandHandler(jda: JDA): CommandHandler() {
    val OWNER_ID = "135726717363945472"
    companion object: KLogging()
    init {
        jda.addEventListener(object : ListenerAdapter() {
            override fun onMessageReceived(event: MessageReceivedEvent) {
                handleMessageCreate(event)
            }
        })
    }
    /**
     * Handles a received message.

     * @param event The MessageReceivedEvent.
     */
    private fun handleMessageCreate(event: MessageReceivedEvent) {
        val jda = event.jda
        if (event.author === jda.selfUser) {
            return
        }
        var splitMessage = event.message.contentRaw.split(" ")
        val commandString = splitMessage[0]
        var command = commands[commandString.toLowerCase()]
        if (command == null) {
            // maybe it requires a mention
            if (splitMessage.size > 1) {
                command = commands[splitMessage[1].toLowerCase()]
                if (command == null || !command.commandAnnotation.requiresMention) {
                    return
                }
                // remove the first which is the mention
                splitMessage = splitMessage.subList(1, splitMessage.size)
            } else {
                return
            }
        }
        val commandAnnotation = command.commandAnnotation
        if (commandAnnotation.requiresMention && commandString != jda.selfUser.asMention) {
            return
        }
        if (event.isFromType(ChannelType.PRIVATE) && !commandAnnotation.privateMessages) {
            return
        }
        if (!event.isFromType(ChannelType.PRIVATE) && !commandAnnotation.channelMessages) {
            return
        }
        if (commandAnnotation.onlyOwner && event.author.id != OWNER_ID) {
            return
        }
        val parameters = getParameters(splitMessage, command, event)
        if (commandAnnotation.async) {
            val commandFinal = command
            val t = Thread(Runnable { invokeMethod(commandFinal, event, parameters) })
            t.isDaemon = true
            t.start()
        } else {
            invokeMethod(command, event, parameters)
        }
    }

    /**
     * Invokes the method of the command.

     * @param command The command.
     * *
     * @param event The event.
     * *
     * @param parameters The parameters for the method.
     */
    private fun invokeMethod(command: CommandHandler.SimpleCommand, event: MessageReceivedEvent, parameters: Array<Any?>) {
        val method = command.method
        var reply: Any? = null
        try {
            reply = method.invoke(command.executor, *parameters)
        } catch (e: IllegalAccessException) {
            logger.error { e }
        } catch (e: InvocationTargetException) {
            logger.error { e }
            e.printStackTrace()
        }

        if (reply != null) {
            event.channel.sendMessage(reply.toString()).queue()
        }
    }

    /**
     * Gets the parameters which are used to invoke the executor's method.

     * @param splitMessage The spit message (index 0: command, index > 0: arguments)
     * *
     * @param command The command.
     * *
     * @param event The event.
     * *
     * @return The parameters which are used to invoke the executor's method.
     */
    private fun getParameters(splitMessage: List<String>, command: SimpleCommand, event: MessageReceivedEvent): Array<Any?> {
        val args = splitMessage.subList(1, splitMessage.size)
        val parameterTypes = command.method.parameterTypes
        val parameters = arrayOfNulls<Any>(parameterTypes.size)
        var stringCounter = 0
        for (i in parameterTypes.indices) { // check all parameters
            val type = parameterTypes[i]
            when (type) {
                String::class.java -> if (stringCounter++ == 0) {
                    parameters[i] = splitMessage[0] // the first split is the command
                } else {
                    if (args.size + 2 > stringCounter) {
                        // the first string parameter is the command, the other ones are the arguments
                        parameters[i] = args[stringCounter - 2]
                    }
                }
                Array<String>::class.java -> parameters[i] = args
                MessageReceivedEvent::class.java -> parameters[i] = event
                JDA::class.java -> parameters[i] = event.jda
                MessageChannel::class.java -> parameters[i] = event.channel
                Message::class.java -> parameters[i] = event.message
                User::class.java -> parameters[i] = event.author
                Member::class.java -> parameters[i] = event.member
                TextChannel::class.java -> parameters[i] = event.textChannel
                PrivateChannel::class.java -> parameters[i] = event.privateChannel
                MessageChannel::class.java -> parameters[i] = event.channel
                Channel::class.java -> parameters[i] = event.textChannel
                Group::class.java -> parameters[i] = event.group
                Guild::class.java -> parameters[i] = event.guild
                Int::class.java, Integer.TYPE -> parameters[i] = event.responseNumber
                Array<Any>::class.java -> parameters[i] = getObjectsFromString(event.jda, args)
                else -> parameters[i] = null
            }
        }
        return parameters
    }

    /**
     * Tries to get objects (like channel, user, integer) from the given strings.

     * @param jda The jda object.
     * *
     * @param args The string array.
     * *
     * @return An object array.
     */
    private fun getObjectsFromString(jda: JDA, args: List<String>): Array<Any?> {
        val objects = arrayOfNulls<Any>(args.size)
        for (i in args.indices) {
            objects[i] = getObjectFromString(jda, args[i])
        }
        return objects
    }

    /**
     * Tries to get an object (like channel, user, integer) from the given string.

     * @param jda The jda object.
     * *
     * @param arg The string.
     * *
     * @return The object.
     */
    private fun getObjectFromString(jda: JDA, arg: String): Any {
        try {
            // test long
            return arg.toLong()
        } catch (e: NumberFormatException) {
        }

        // test user
        if (arg.matches("<@([0-9]*)>".toRegex())) {
            val id = arg.substring(2, arg.length - 1)
            val user = jda.getUserById(id)
            if (user != null) {
                return user
            }
        }
        // test channel
        if (arg.matches("<#([0-9]*)>".toRegex())) {
            val id = arg.substring(2, arg.length - 1)
            val channel = jda.getTextChannelById(id)
            if (channel != null) {
                return channel
            }
        }
        return arg
    }
}
