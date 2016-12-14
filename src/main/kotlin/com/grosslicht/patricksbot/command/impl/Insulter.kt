package com.grosslicht.patricksbot.command.impl

import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import com.grosslicht.patricksbot.extensions.random
import mu.KLogging
import net.dv8tion.jda.core.entities.Message

/**
 * Created by patrickgrosslicht on 14/12/16.
 */
class Insulter : CommandExecutor {
    companion object: KLogging()

    abstract class InsultGenerator {
        abstract val words : List<Array<String>>
        open fun generateInsult() : String = words.asSequence().map { w : Array<String> -> w.random() }.joinToString(" ")
    }

    class ShakespeareanInsultGenerator : InsultGenerator() {
        val column1 = arrayOf(
                "artless",     "bawdy",      "beslubbering", "bootless",
                "churlish",    "cockered",   "clouted",      "craven",
                "currish",     "dankish",    "dissembling",  "droning",
                "errant",      "fawning",    "fobbing",      "froward",
                "frothy",      "gleeking",   "goatish",      "gorbellied",
                "impertinent", "infectious", "jarring",      "loggerheaded",
                "lumpish",     "mammering",  "mangled",      "mewling",
                "paunchy",     "pribbling",  "puking",       "puny",
                "qualling",    "rank",       "reeky",        "roguish",
                "ruttish",     "saucy",      "spleeny",      "spongy",
                "surly",       "tottering",  "unmuzzled",    "vain",
                "venomed",     "villainous", "warped",       "wayward",
                "weedy",       "yeasty",     "cullionly",    "fusty",
                "caluminous",  "wimpled",    "burly-boned",  "misbegotten",
                "odiferous",   "poisonous",  "fishified",    "Wart-necked"
        )

        val column2 = arrayOf(
                "base-court",     "bat-fowling",     "beef-witted",    "beetle-headed",
                "boil-brained",   "clapper-clawed",  "clay-brained",   "common-kissing",
                "crook-pated",    "dismal-dreaming", "dizzy-eyed",     "doghearted",
                "dread-bolted",   "earth-vexing",    "elf-skinned",    "fat-kidneyed",
                "fen-sucked",     "flap-mouthed",    "fly-bitten",     "folly-fallen",
                "fool-born",      "full-gorged",     "guts-griping",   "half-faced",
                "hasty-witted",   "hedge-born",      "hell-hated",     "idle-headed",
                "ill-breeding",   "ill-nurtured",    "knotty-pated",   "milk-livered",
                "motley-minded",  "onion-eyed",      "plume-plucked",  "pottle-deep",
                "pox-marked",     "reeling-ripe",    "rough-hewn",     "rude-growing",
                "rump-fed",       "shard-borne",     "sheep-biting",   "spur-galled",
                "swag-bellied",   "tardy-gaited",    "tickle-brained", "toad-spotted",
                "urchin-snouted", "weather-bitten",  "whoreson",       "malmsey-nosed",
                "rampallian",     "lily-livered",    "scurvy-valiant", "brazen-faced",
                "unwash'd",       "bunch-back'd",    "leaden-footed",  "muddy-mettled"
        )

        val column3 = arrayOf(
                "apple-john",  "baggage",       "barnacle",    "bladder",
                "boar-pig",    "bugbear",       "bum-bailey",  "canker-blossom",
                "clack-dish",  "clotpole",      "coxcomb",     "codpiece",
                "death-token", "dewberry",      "flap-dragon", "flax-wench",
                "flirt-gill",  "foot-licker",   "fustilarian", "giglet",
                "gudgeon",     "haggard",       "harpy",       "hedge-pig",
                "horn-beast",  "hugger-mugger", "joithead",    "lewdster",
                "lout",        "maggot-pie",    "malt-worm",   "mammet",
                "measle",      "minnow",        "miscreant",   "moldwarp",
                "mumble-news", "nut-hook",      "pigeon-egg",  "pignut",
                "puttock",     "pumpion",       "ratsbane",    "scut",
                "skainsmate",  "strumpet",      "varlot",      "vassal",
                "whey-face",   "wagtail",       "knave",       "blind-worm",
                "popinjay",    "scullian",      "jolt-head",   "malcontent",
                "devil-monk",  "toad",          "rascal",      "Basket-Cockle"
        )

        override val words = listOf(column1, column2, column3)
    }

    val generator = ShakespeareanInsultGenerator()

    @Command(aliases = arrayOf(".insult"), showInHelpPage = true, usage = ".insult [@user]")
    fun insult(message: Message) : String {
        if (message.mentionedUsers.isEmpty()) {
            return "Thou shalt mention an ignaro, thou ${generator.generateInsult()}!"
        }
        return message.mentionedUsers.map { u -> u.name }.joinToString(", ")
                .plus(" ${if (message.mentionedUsers.size > 1) "ye" else "thou"} ${generator.generateInsult()}${if (message.mentionedUsers.size > 1) "s!" else "!"}")
    }
}



