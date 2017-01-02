package com.grosslicht.patricksbot.models

import io.requery.*
import io.requery.query.Result
import java.time.OffsetDateTime

/**
 * Created by patrickgrosslicht on 15/12/16.
 */
@Entity
interface Message : Persistable {
    @get:Key @get:Column(length = 20)
    val id: String

    val time: OffsetDateTime

    @get:ManyToOne @get:Column(length = 20)
    val channel: Channel

    @get:ManyToOne @get:Column(length = 20)
    val user: User

    @get:Column(length = 2000)
    var content: String

    @get:OneToMany
    val revisions: Result<MessageRevision>

    @get:OneToMany @get:Column(length = 20)
    val attachments: MutableSet<MessageAttachment>

    var isDeleted: Boolean

    var isEdited: Boolean
}