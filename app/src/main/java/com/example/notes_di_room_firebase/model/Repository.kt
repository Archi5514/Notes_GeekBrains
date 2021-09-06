package com.example.notes_di_room_firebase.model

import com.example.notes_di_room_firebase.model.providers.FireStoreProvider
import com.example.notes_di_room_firebase.model.providers.RemoteDataProvider

object Repository {

    private val remoteDataProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteDataProvider.subscribeToAllNotes()

    fun saveNote(note: Note) = remoteDataProvider.saveNote(note = note)

    fun getNoteById(id: String) = remoteDataProvider.getNoteById(id = id)

    fun getCurrentUser() = remoteDataProvider.getCurrentUser()

}