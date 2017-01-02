package com.grosslicht.patricksbot.models

import io.requery.*

/**
 * Created by patrickgrosslicht on 15/12/16.
 */
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
