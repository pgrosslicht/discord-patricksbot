package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import com.grosslicht.patricksbot.command.CommandHandler
import de.vandermeer.asciitable.v2.V2_AsciiTable
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer
import de.vandermeer.asciitable.v2.render.WidthAbsoluteEven
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes
import mu.KLogging
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message

/**
 * Created by patrickgrosslicht on 12/11/16.
 */
class HelpCommand(private val commandHandler: CommandHandler) : CommandExecutor {
    companion object: KLogging()

    @Command(aliases = arrayOf(".help"), usage = ".help <theme>", description = "Shows this page.")
    fun onHelpCommand(message: Message): String {
        val builder = MessageBuilder()
        val table = V2_AsciiTable()
        table.addRule()
        table.addRow("Command", "Usage", "Description")
        table.addRule()
        commandHandler.getCommands()
                .filter { it.commandAnnotation.showInHelpPage }
                .forEach {
                    table.addRow(it.commandAnnotation.aliases[0], if (it.commandAnnotation.usage.isEmpty()) it.commandAnnotation.aliases[0] else it.commandAnnotation.usage, it.commandAnnotation.description)
                    table.addRule()
                }
        val renderer = V2_AsciiTableRenderer()

        when {
            message.content.contains("double-light") -> renderer.setTheme(V2_E_TableThemes.UTF_DOUBLE_LIGHT.get())
            message.content.contains("light-double") -> renderer.setTheme(V2_E_TableThemes.UTF_LIGHT_DOUBLE.get())
            message.content.contains("double") -> renderer.setTheme(V2_E_TableThemes.UTF_DOUBLE.get())
            message.content.contains("heavy") -> renderer.setTheme(V2_E_TableThemes.UTF_HEAVY.get())
            else -> renderer.setTheme(V2_E_TableThemes.UTF_LIGHT.get())
        }
        renderer.setWidth(WidthAbsoluteEven(85))
        builder.appendCodeBlock(renderer.render(table).toStrBuilder(), "xml")
        return builder.build().content
    }

}
