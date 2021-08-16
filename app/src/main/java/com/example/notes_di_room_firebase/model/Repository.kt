package com.example.notes_di_room_firebase.model

import java.util.*

object Repository {

    private val notes: MutableList<Note> = mutableListOf(
        Note(
            id = UUID.randomUUID().toString(),
            title = "Kotlin",
            body = "Suck some dick!",
            color = Color.RED
        )
    )

    fun getNotes(): List<Note> = notes
}