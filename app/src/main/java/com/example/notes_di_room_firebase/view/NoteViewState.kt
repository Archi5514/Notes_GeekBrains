package com.example.notes_di_room_firebase.view

import com.example.notes_di_room_firebase.model.Note

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(data = note, error = error)