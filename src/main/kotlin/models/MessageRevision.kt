package com.grosslicht.patricksbot.models

import io.requery.*
import java.time.OffsetDateTime


@Entity
interface MessageRevision : Persistable {
    @get:Key @get:Generated
    val id: Int

    @get:ManyToOne @get:Column(length = 20)
    val message: Message

    val time: OffsetDateTime

    @get:Column(length = 2000)
    val content: String
}