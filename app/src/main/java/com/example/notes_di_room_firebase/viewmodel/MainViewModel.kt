package com.example.notes_di_room_firebase.viewmodel

import androidx.lifecycle.Observer
import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.model.NoteResult
import com.example.notes_di_room_firebase.model.Repository
import com.example.notes_di_room_firebase.view.MainViewState

class MainViewModel(repository: Repository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val repositoryNotes = repository.getNotes()
    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(result: NoteResult?) {
            if(result == null) return

            when(result) {
                is NoteResult.Success<*> -> viewStateLiveData.value = MainViewState(notes = result.data as List<Note>)
                is NoteResult.Error -> viewStateLiveData.value = MainViewState(error = result.error)
            }
        }
    }

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}