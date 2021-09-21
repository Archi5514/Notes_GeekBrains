package com.example.notes_di_room_firebase.model.providers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notes_di_room_firebase.model.NoAuthException
import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.model.NoteResult
import com.example.notes_di_room_firebase.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider(private val db: FirebaseFirestore, private val firebaseAuth: FirebaseAuth) :
    RemoteDataProvider {

    private val currentUser
        get() = firebaseAuth.currentUser

    override fun subscribeToAllNotes(): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUserNotesCollection().addSnapshotListener { snapshot, exception ->
                    value = exception?.let {
                        throw it
                    } ?: snapshot?.let {
                        NoteResult.Success(data = it.documents.map { documentSnapshot ->
                            documentSnapshot.toObject(
                                Note::class.java
                            )
                        })
                    }
                }
            } catch (exception: Throwable) {
                value = NoteResult.Error(error = exception)
            }
        }

    override fun getNoteById(id: String): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUserNotesCollection().document(id).get()
                    .addOnSuccessListener { snapshot ->
                        value = NoteResult.Success(snapshot.toObject(Note::class.java))
                    }
                    .addOnFailureListener {
                        throw it
                    }
            } catch (exception: Throwable) {
                value = NoteResult.Error(error = exception)
            }
        }

    override fun saveNote(note: Note): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUserNotesCollection().document(note.id).set(note)
                    .addOnSuccessListener {
                        Log.d(TAG, "Note $note is saved")
                        value = NoteResult.Success(data = note)
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Error saving note $note, message: ${it.message}")
                        throw it
                    }
            } catch (exception: Throwable) {
                value = NoteResult.Error(error = exception)
            }
        }

    override fun getCurrentUser(): LiveData<User?> =
        MutableLiveData<User?>().apply {
            value = currentUser?.let {
                User(
                    name = it.displayName ?: "",
                    email = it.email ?: ""
                )
            }
        }

    override fun deleteNote(id: String): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                getUserNotesCollection().document(id).delete()
                    .addOnSuccessListener {
                        value = NoteResult.Success(data = null)
                    }
                    .addOnFailureListener {
                        throw it
                    }
            } catch (exception: Throwable) {
                value = NoteResult.Error(error = exception)
            }
        }

    private fun getUserNotesCollection() = currentUser?.let { firebaseUser ->
        db.collection(USERS_COLLECTION)
            // document означает взять юзера по критерии (тому, что в скобках)
            .document(firebaseUser.uid)
            // создание подколлекции в документе с названием документа firebaseUser.uid и названием коллекции NOTES_COLLECTION
            .collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    companion object {
        private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    }
}