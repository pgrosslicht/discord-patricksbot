package com.grosslicht.patricksbot.models

import io.requery.*
import io.requery.query.Result


@Entity
interface Guild : Persistable {
    @get:Key @get:Column(length = 20)
    val id: String

    @get:Column(length = 100)
    val name: String

    @get:ManyToOne @get:Column(length = 20)
    val owner: User

    @get:OneToMany
    val channels: Result<Channel>

    @get:OneToMany
    val emotes: Result<Emote>
}