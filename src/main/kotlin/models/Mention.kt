package com.grosslicht.patricksbot.models

import io.requery.*


@Entity
interface Mention {
    @get:Key @get:Generated
    val id: Int

    @get:ManyToOne @get:Column(length = 20)
    val message: Message

    @get:Column(length = 20) @get:ManyToOne
    val user: User
}