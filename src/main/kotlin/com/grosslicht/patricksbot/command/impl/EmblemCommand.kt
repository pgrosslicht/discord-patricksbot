package com.grosslicht.patricksbot.command.impl

import com.github.salomonbrys.kotson.fromJson
import com.google.firebase.database.*
import com.google.gson.Gson
import com.grosslicht.patricksbot.command.Command
import com.grosslicht.patricksbot.command.CommandExecutor
import com.grosslicht.patricksbot.models.Emblem
import mu.KLogging
import org.apache.commons.math3.random.MersenneTwister
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

/**
 * Created by patrickgrosslicht on 16/11/16.
 */
class EmblemCommand : CommandExecutor {
    companion object : KLogging()
    data class Game(var seed: Long = 0, var rounds: Int = 0)
    val EMBLEM_URL = "https://raw.githubusercontent.com/pdgwien/mtg-emblems/master/app/data/emblems.json"
    val emblems: List<Emblem> = parseEmblemsFromUrl()
    val firebase = FirebaseDatabase.getInstance()
    var ref: DatabaseReference? = null
    var listener: ValueEventListener? = null
    var twister: MersenneTwister? = null

    @Command(aliases = arrayOf(".join"), description = "Joins a game of MtG-Emblems")
    fun onJoinCommand(params: Array<Any>): String {
        ref = firebase.getReference("${params.first()}/metadata")
        listener = object : ValueEventListener {
            val twister: MersenneTwister = MersenneTwister()
            var initialized = false
            override fun onCancelled(databaseError: DatabaseError) {
                logger.debug { "Firebase error: $databaseError" }
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val game: Game = dataSnapshot.getValue<Game>(Game::class.java)
                logger.debug { dataSnapshot }
                if (!initialized) {
                    twister.setSeed(game.seed)
                    initialized = true
                } else {
                    val rng = twister.nextLong(emblems.size.toLong())
                    logger.debug { "$rng - ${emblems[rng.toInt()]}" }
                }
            }
        }
        ref?.addValueEventListener(listener)
        return "Successfully joined the game! You will now be updated about any progress."
    }

    fun readUrl(urlString: String): String {
        var reader: BufferedReader? = null
        try {
            val url = URL(urlString)
            reader = BufferedReader(InputStreamReader(url.openStream()))
            val buffer = StringBuffer()
            for (line in reader.lines()) {
                buffer.append(line)
            }
            return buffer.toString()
        } finally {
            if (reader != null)
                reader.close()
        }
    }

    fun parseEmblemsFromUrl(): List<Emblem> {
        return Gson().fromJson<List<Emblem>>(readUrl(EMBLEM_URL))
    }
}
