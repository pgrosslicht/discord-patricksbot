package com.grosslicht.patricksbot.command.impl

import com.github.kittinunf.fuel.httpPost
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import mu.KLogging
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import org.apache.commons.codec.binary.Base64
import java.time.Instant
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
        val secretToken = "76r5vn7gvp76xtj1"
        init {
            val hmac = Mac.getInstance("HmacSHA256")
            hmac.init(SecretKeySpec(secretToken.toByteArray(), "HmacSHA256"))
            mac = Base64.encodeBase64String(hmac.doFinal(timeCreated.toString().toByteArray()))
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

    fun executeCode(language: String, code: String) : String {
        val token = Token()
        var string = StringBuilder()
        var output = MessageBuilder()
        "http://httpbin.org/post".httpPost(listOf(Pair("auth", "${token.timeCreated}:${token.mac}"), Pair("language", language), Pair("code", code)))
                .header(mapOf("Content-Type" to "application/x-www-form-urlencoded", "Accept" to "application/json"))
                .responseString { request, response, result ->
                result.fold({ d ->
                    logger.debug { d }
                    val results = Gson().fromJson<List<CodeResult>>(d)
                    logger.debug { results }
                    for ((command, data, error) in results) {
                        if (error !== "") {
                            output.appendCodeBlock(error, language)
                        }
                        if (command == "result") {
                            string.append(data)
                        } else if (command == "output") {
                            output.appendCodeBlock(string.toString(), language)
                        }
                    }
                }, { err ->
                    output.appendString("Error while executing code")
                    logger.debug { err }
                })
            }
        return output.build().toString()
    }

    @Command(aliases = arrayOf(".code"), description = "Compiles code")
    fun handleCommand(message: Message): String {
        if (message.content == ".code supported") {
            return supportedLanguages.joinToString(", ")
        } else if (message.content.startsWith(".code ```")) {
            val lang = getLanguage(message.content)
            if (languageAliases[lang] == null) {
                return "This language is not supported, type `.code supported` to see the supported languages."
            }
            return executeCode(lang, message.content.substring(message.content.indexOf("```"), message.content.lastIndexOf("```") + 3).replace(Regex("(^```(\\w+)?)|(```$)"), "").trim())
        }
        return "Usage: `.code ```language codeToExecute```"
    }
}
