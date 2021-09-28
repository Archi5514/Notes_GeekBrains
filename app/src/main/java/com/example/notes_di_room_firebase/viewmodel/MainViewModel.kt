package com.example.notes_di_room_firebase.viewmodel

import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.model.NoteResult
import com.example.notes_di_room_firebase.model.Repository
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(repository: Repository) : BaseViewModel<List<Note>?>() {

    private val notesChannel by lazy { runBlocking { repository.getNotes() } }

    init {
        launch {
            notesChannel.consumeEach { result ->
                when (result) {
                    is NoteResult.Success<*> -> setData(result.data as List<Note>)
                    is NoteResult.Error -> setError(result.error)
                }
            }
        }
    }

    override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }
}