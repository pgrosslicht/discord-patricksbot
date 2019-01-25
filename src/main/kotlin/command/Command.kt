package com.grosslicht.patricksbot.command



@Retention(AnnotationRetention.RUNTIME)
annotation class Command(
        /**
         * Gets whether the executor should listen to private messages or not.
         */
        val privateMessages: Boolean = true,
        /**
         * Gets whether the executor should listen to channel messages or not.
         */
        val channelMessages: Boolean = true,
        /**
         * Gets the commands the executor should listen to. The first element is the main command.
         */
        val aliases: Array<String> = arrayOf(""),
        /**
         * Gets the description of the command.
         */
        val description: String = "none",
        /**
         * Gets the usage of the command.
         * If no usage was provided it will use the first alias.
         */
        val usage: String = "",
        /**
         * Gets whether the command is only for admins or not.
         */
        val onlyOwner: Boolean = false,
        /**
         * Gets whether the command should be shown in the help page or not.
         */
        val showInHelpPage: Boolean = true,
        /**
         * Gets whether the command should be executed async or not. If not the thread of the message listener is used.
         */
        val async: Boolean = true,
        /**
         * Gets whether the bot has to be mentioned to react to a command.
         * This would look like <code>@botname alias</code>
         */
        val requiresMention: Boolean = false
)
