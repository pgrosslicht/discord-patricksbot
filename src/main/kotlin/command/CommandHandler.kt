package com.grosslicht.patricksbot.command

import java.lang.reflect.Method
import java.util.*


abstract class CommandHandler {
    val commands: HashMap<String, SimpleCommand> = HashMap()
    private val commandList: MutableList<SimpleCommand> = ArrayList()

    /**
     * Gets the default command prefix.
     */
    private var defaultPrefix = ""

    /**
     * Registers an executor.

     * @param executor The executor to register.
     */
    fun registerCommand(executor: CommandExecutor) {
        for (method in executor.javaClass.methods) {
            val annotation = method.getAnnotation(Command::class.java) ?: continue
            if (annotation.aliases.isEmpty()) {
                throw IllegalArgumentException("Aliases array cannot be empty!")
            }
            val command = SimpleCommand(annotation, method, executor)
            for (alias in annotation.aliases) {
                // add command to map. It's faster to access it from the map than iterating to the whole list
                commands[defaultPrefix + alias.toLowerCase().replace(" ", "")] = command
            }
            // we need a list, too, because a HashMap is not ordered.
            commandList.add(command)
        }
    }

    /**
     * Gets a list with all commands in the order they were registered.
     * This is useful for automatic help commands.

     * @return A list with all commands the the order they were registered.
     */
    fun getCommands(): List<SimpleCommand> {
        return Collections.unmodifiableList(commandList)
    }

    /**
     * A simple representation of a command.
     */
    inner class SimpleCommand
    /**
     * Class constructor.

     * @param commandAnnotation The annotation of the executor's method.
     * *
     * @param method The method which listens to the commands.
     * *
     * @param executor The executor of the method.
     */
    constructor(
            /**
             * The command annotation of the method.

             * @return The command annotation of the method.
             */
            val commandAnnotation: Command,
            /**
             * Gets the method which listens to the commands.

             * @return The method which listens to the commands.
             */
            val method: Method,
            /**
             * Gets the executor of the method.

             * @return The executor of the method.
             */
            val executor: CommandExecutor
    )
}
