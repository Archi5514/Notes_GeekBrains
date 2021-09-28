package com.example.notes_di_room_firebase.view

import com.example.notes_di_room_firebase.model.Note

data class NoteData(val isDeleted: Boolean = false, val note: Note? = null, val error: Throwable? = null)