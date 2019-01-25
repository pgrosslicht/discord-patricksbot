package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import com.grosslicht.patricksbot.command.CommandHandler
import de.vandermeer.asciitable.AsciiTable
import mu.KLogging
import net.dv8tion.jda.core.MessageBuilder


class HelpCommand(private val commandHandler: CommandHandler) : CommandExecutor {
    companion object : KLogging()

    @Command(aliases = [".help"], usage = ".help", description = "Shows this page.")
    fun onHelpCommand(): String {
        val builder = MessageBuilder()
        val table = AsciiTable()
        table.addRule()
        table.addRow("Command", "Usage", "Description")
        table.addRule()
        commandHandler.getCommands()
                .filter { it.commandAnnotation.showInHelpPage }
                .forEach {
                    table.addRow(
                            it.commandAnnotation.aliases[0],
                            if (it.commandAnnotation.usage.isEmpty()) it.commandAnnotation.aliases[0] else it.commandAnnotation.usage,
                            it.commandAnnotation.description
                    )
                    table.addRule()
                }

        table.context.width = 85
        builder.appendCodeBlock(table.render(), "xml")
        return builder.build().contentRaw
    }

}
