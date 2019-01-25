package com.grosslicht.patricksbot.models

import io.requery.*


@Entity
interface Channel : Persistable {
    @get:Key
    @get:Column(length = 20)
    val id: String

    @get:ManyToOne @get:Column(length = 20)
    val guild: Guild

    @get:Column(length = 100)
    val name: String
}
