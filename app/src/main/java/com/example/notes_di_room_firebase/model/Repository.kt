package com.example.notes_di_room_firebase.model

import com.example.notes_di_room_firebase.model.providers.RemoteDataProvider

class Repository(private val remoteDataProvider: RemoteDataProvider) {

    suspend fun getNotes() = remoteDataProvider.subscribeToAllNotes()

    suspend fun saveNote(note: Note) = remoteDataProvider.saveNote(note = note)

    suspend fun getNoteById(id: String) = remoteDataProvider.getNoteById(id = id)

    suspend fun getCurrentUser() = remoteDataProvider.getCurrentUser()

    suspend fun deleteNote(id: String) = remoteDataProvider.deleteNote(id = id)

}