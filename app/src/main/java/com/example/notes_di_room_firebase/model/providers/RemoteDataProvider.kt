package com.example.notes_di_room_firebase.model.providers

import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.model.NoteResult
import com.example.notes_di_room_firebase.model.User
import kotlinx.coroutines.channels.ReceiveChannel
import java.lang.Exception

interface RemoteDataProvider {

    suspend fun subscribeToAllNotes(): ReceiveChannel<NoteResult>

    suspend fun getNoteById(id: String): Note

    suspend fun saveNote(note: Note): Note

    suspend fun getCurrentUser(): User?

    suspend fun deleteNote(id: String): Exception?

}