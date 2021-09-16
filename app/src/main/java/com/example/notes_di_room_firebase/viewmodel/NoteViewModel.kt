package com.example.notes_di_room_firebase.viewmodel

import androidx.lifecycle.Observer
import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.model.NoteResult
import com.example.notes_di_room_firebase.model.Repository
import com.example.notes_di_room_firebase.view.NoteViewState

class NoteViewModel(private val repository: Repository = Repository) :
    BaseViewModel<Note?, NoteViewState>() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let { pendingNote -> repository.saveNote(pendingNote) }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(id = noteId).observeForever(object : Observer<NoteResult> {
            override fun onChanged(result: NoteResult?) {
                if (result == null) return

                when (result) {
                    is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(note = result.data as Note)
                    is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
                }
            }
        })
    }

}