package com.example.notes_di_room_firebase.viewmodel

import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.model.Repository
import com.example.notes_di_room_firebase.view.NoteData
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: Repository) :
    BaseViewModel<NoteData>() {

    private val currentNote: Note?
        get() = getViewState().poll()?.note

    override fun onCleared() {
        launch {
            currentNote?.let { repository.saveNote(it) }
            super.onCleared()
        }
    }

    fun saveChanges(note: Note) {
        setData(NoteData(note = note))
    }

    fun loadNote(noteId: String) {
        launch {
            try {
                setData(NoteData(note = repository.getNoteById(id = noteId)))
            } catch (exception: Throwable) {
                setError(exception)
            }
        }
    }

    fun deleteNote() {
        launch {
            try {
                currentNote?.let { repository.deleteNote(it.id) }
                setData(NoteData(isDeleted = true))
            } catch (exception: Throwable) {
                setError(exception)
            }
        }
    }

}