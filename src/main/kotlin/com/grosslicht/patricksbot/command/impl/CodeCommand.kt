package com.grosslicht.patricksbot.command.impl

import com.github.kittinunf.fuel.httpPost
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import mu.KLogging
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageChannel
import java.net.URLEncoder
import java.time.Instant
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


/**
 * Created by patrickgrosslicht on 23/11/16.
 */
class CodeCommand : CommandExecutor {
    companion object : KLogging()
    data class CodeResult(val command: String, val data: String, val error: String)
    class Token {
        val timeCreated = Instant.now().toEpochMilli()
        var mac: String
        val secretToken: String = System.getenv("REPL_IT_SECRET_TOKEN")

        init {
            val hmac = Mac.getInstance("HmacSHA256")
            hmac.init(SecretKeySpec(secretToken.toByteArray(), "HmacSHA256"))
            mac = Base64.getEncoder().encodeToString(hmac.doFinal(timeCreated.toString().toByteArray()))
        }
    }

    val languageAliases = hashMapOf(Pair("python3", "python3"),
            Pair("python", "python"),
            Pair("ruby", "ruby"),
            Pair("php", "php"),
            Pair("nodejs", "nodejs"),
            Pair("node", "nodejs"),
            Pair("javascript", "nodejs"),
            Pair("js", "nodejs"),
            Pair("java", "java"),
            Pair("cpp11", "cpp11"),
            Pair("c++11", "cpp11"),
            Pair("cpp", "cpp"),
            Pair("c++", "cpp"),
            Pair("c", "c"),
            Pair("csharp", "csharp"),
            Pair("c#", "csharp"),
            Pair("fsharp", "fsharp"),
            Pair("f#", "fsharp"),
            Pair("rust", "rust"),
            Pair("swift", "swift"),
            Pair("gloang", "go"),
            Pair("go", "go"),
            Pair("lua", "lua"))

    val supportedLanguages = listOf("C#", "C", "C++", "C++ 11", "F#", "Go", "Java", "Lua", "Nodejs", "PHP", "Python", "Python 3", "Ruby", "Rust", "Swift")

    fun getLanguage(msg: String) = msg.substring(msg.indexOf("```") + 3, msg.indexOf("\n"))

    fun executeCode(channel: MessageChannel, language: String, code: String) {
        val token = Token()
        var output = MessageBuilder()
        "https://api.repl.it/eval?auth=${token.timeCreated}:${URLEncoder.encode(token.mac, "UTF-8")}&language=$language".httpPost(listOf(Pair("code", code)))
                .header(mapOf("Content-Type" to "application/x-www-form-urlencoded", "Accept" to "application/json"))
                .responseString { request, response, result ->
                    result.fold({ d ->
                        val results = Gson().fromJson<List<CodeResult>>(d)
                        output = output.append("```$language\n")
                        for ((command, data, error) in results) {
                            if (error != "") {
                                output = output.append(error)
                            }
                            if (command == "output") {
                                output = output.append(data)
                            } else if (command == "result") {
                                output = output.append("```")
                                channel.sendMessage(output.build()).queue()
                            }
                        }
                    }, { err ->
                        output.append("Error while executing code")
                        channel.sendMessage(output.build()).queue()
                        logger.error { err }
                    })
                }
    }

    @Command(aliases = arrayOf(".code"), description = "Compiles code", async = true)
    fun handleCommand(message: Message) {
        if (message.contentDisplay == ".code supported") {
            message.channel.sendMessage(supportedLanguages.joinToString(", ")).queue()
        } else if (message.contentDisplay.startsWith(".code ```")) {
            val lang = getLanguage(message.contentDisplay)
            if (languageAliases[lang] == null) {
                message.channel.sendMessage("This language is not supported, type `.code supported` to see the supported languages.").queue()
            } else {
                executeCode(message.channel, languageAliases[lang]!!, message.contentDisplay.substring(message.contentDisplay.indexOf("```"), message.contentDisplay.lastIndexOf("```") + 3).replace(Regex("(^```(\\w+)?)|(```$)"), "").trim())
            }
        } else {
            message.channel.sendMessage("Usage: `.code ```language codeToExecute```").queue()
        }
    }
}
