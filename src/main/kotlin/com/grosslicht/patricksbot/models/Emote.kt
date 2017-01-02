package com.grosslicht.patricksbot.models

import io.requery.*

/**
 * Created by patrickgrosslicht on 15/12/16.
 */
@Entity
interface Emote : Persistable {
    @get:Key @get:Column(length = 20)
    val id: String

    @get:ManyToOne @get:Column(length = 20)
    val guild: Guild

    val name: String
}