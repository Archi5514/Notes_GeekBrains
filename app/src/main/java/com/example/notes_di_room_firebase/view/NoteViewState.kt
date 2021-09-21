package com.example.notes_di_room_firebase.view

import com.example.notes_di_room_firebase.model.Note

class NoteViewState(data: Data = Data(), error: Throwable? = null) :
    BaseViewState<NoteViewState.Data>(data = data, error = error) {

        data class Data(val isDeleted: Boolean = false, val note: Note? = null)

    }