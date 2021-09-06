package com.example.notes_di_room_firebase.model.providers

import androidx.lifecycle.LiveData
import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.model.NoteResult
import com.example.notes_di_room_firebase.model.User

interface RemoteDataProvider {

    fun subscribeToAllNotes(): LiveData<NoteResult>

    fun getNoteById(id: String): LiveData<NoteResult>

    fun saveNote(note: Note): LiveData<NoteResult>

    fun getCurrentUser(): LiveData<User?>
}