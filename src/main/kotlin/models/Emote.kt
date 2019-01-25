package com.grosslicht.patricksbot.models

import io.requery.*


@Entity
interface Emote : Persistable {
    @get:Key
    @get:Column(length = 20)
    val id: String

    @get:ManyToOne
    @get:Column(length = 20)
    val guild: Guild

    val name: String
}