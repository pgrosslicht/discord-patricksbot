package com.grosslicht.patricksbot.models

import io.requery.Column
import io.requery.Entity
import io.requery.Key
import io.requery.Persistable


@Entity
interface User : Persistable {
    @get:Key @get:Column(length = 20)
    val id: String

    @get:Column(length = 32)
    val avatarId: String

    val isBot: Boolean

    @get:Column(length = 32)
    val name: String

    @get:Column(length = 6)
    val discriminator: String
}