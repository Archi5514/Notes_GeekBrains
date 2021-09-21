package com.example.notes_di_room_firebase.viewmodel

import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.model.NoteResult
import com.example.notes_di_room_firebase.model.Repository
import com.example.notes_di_room_firebase.view.NoteViewState

class NoteViewModel(private val repository: Repository) :
    BaseViewModel<NoteViewState.Data, NoteViewState>() {

    private val currentNote: Note?
        get() = viewStateLiveData.value?.data?.note

    override fun onCleared() {
        currentNote?.let { repository.saveNote(it) }
    }

    fun saveChanges(note: Note) {
        viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = note))
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(id = noteId).observeForever { result ->
            result.let {
                viewStateLiveData.value = when (it) {
                    is NoteResult.Success<*> ->
                        NoteViewState(data = NoteViewState.Data(note = it.data as Note))
                    is NoteResult.Error ->
                        NoteViewState(error = it.error)
                }
            }
        }
    }

    fun deleteNote() {
        currentNote?.let { note ->
            repository.deleteNote(id = note.id).observeForever { result ->
                result?.let {
                    viewStateLiveData.value = when (it) {
                        is NoteResult.Success<*> ->
                            NoteViewState(data = NoteViewState.Data(isDeleted = true))
                        is NoteResult.Error ->
                            NoteViewState(error = it.error)
                    }
                }
            }
        }
    }

}