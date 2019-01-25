package com.grosslicht.patricksbot.models

import io.requery.*


@Entity
interface MessageAttachment : Persistable {
    @get:Key
    @get:Column(length = 20)
    val id: String

    @get:ManyToOne
    @get:Column(length = 20)
    val message: Message

    val url: String
    val proxyUrl: String
    val fileName: String
}