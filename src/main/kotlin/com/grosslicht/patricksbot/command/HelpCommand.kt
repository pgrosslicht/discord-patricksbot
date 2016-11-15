package com.grosslicht.patricksbot.command

/**
 * Created by patrickgrosslicht on 12/11/16.
 */
class HelpCommand(private val commandHandler: CommandHandler) : CommandExecutor {

    @Command(aliases = arrayOf(".help", ".commands"), description = "Shows this page")
    fun onHelpCommand(): String {
        val builder = StringBuilder()
        builder.append("```xml") // a xml code block looks fancy
        for (simpleCommand in commandHandler.getCommands()) {
            if (!simpleCommand.commandAnnotation.showInHelpPage) {
                continue // skip command
            }
            builder.append("\n")
            if (!simpleCommand.commandAnnotation.requiresMention) {
                // the default prefix only works if the command does not require a mention
                builder.append(commandHandler.defaultPrefix)
            }
            var usage = simpleCommand.commandAnnotation.usage
            if (usage.isEmpty()) { // no usage provided, using the first alias
                usage = simpleCommand.commandAnnotation.aliases[0]
            }
            builder.append(usage)
            val description = simpleCommand.commandAnnotation.description
            if (description != "none") {
                builder.append(" | ").append(description)
            }
        }
        builder.append("\n```") // end of xml code block
        return builder.toString()
    }

}
