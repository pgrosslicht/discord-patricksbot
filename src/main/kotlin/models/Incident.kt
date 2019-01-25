package com.grosslicht.patricksbot.models

import io.requery.*
import java.time.ZonedDateTime


@Entity
interface Incident : Persistable {
    @get:Key @get:Generated
    val id: Int

    @get:ManyToOne @get:Column(length = 20)
    val user: User

    val offlineTime: ZonedDateTime

    val onlineTime: ZonedDateTime
}
