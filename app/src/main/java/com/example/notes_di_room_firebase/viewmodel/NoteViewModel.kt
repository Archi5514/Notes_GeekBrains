package com.example.notes_di_room_firebase.viewmodel

import androidx.lifecycle.ViewModel
import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.model.Repository

class NoteViewModel(private val repository: Repository = Repository) : ViewModel() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let { pendingNote -> repository.saveNote(pendingNote) }

    }

}