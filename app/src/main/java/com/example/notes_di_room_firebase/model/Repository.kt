package com.example.notes_di_room_firebase.model

import com.example.notes_di_room_firebase.model.providers.FireStoreProvider
import com.example.notes_di_room_firebase.model.providers.RemoteDataProvider

class Repository(private val remoteDataProvider: RemoteDataProvider) {

    fun getNotes() = remoteDataProvider.subscribeToAllNotes()

    fun saveNote(note: Note) = remoteDataProvider.saveNote(note = note)

    fun getNoteById(id: String) = remoteDataProvider.getNoteById(id = id)

    fun getCurrentUser() = remoteDataProvider.getCurrentUser()

    fun deleteNote(id: String) = remoteDataProvider.deleteNote(id = id)

}