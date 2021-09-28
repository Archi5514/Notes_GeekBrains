package com.example.notes_di_room_firebase.model.providers

import android.util.Log
import kotlinx.coroutines.channels.ReceiveChannel
import androidx.lifecycle.MutableLiveData
import com.example.notes_di_room_firebase.model.NoAuthException
import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.model.NoteResult
import com.example.notes_di_room_firebase.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider(private val db: FirebaseFirestore, private val firebaseAuth: FirebaseAuth) :
    RemoteDataProvider {

    private val currentUser
        get() = firebaseAuth.currentUser

    override suspend fun subscribeToAllNotes(): ReceiveChannel<NoteResult> =
        Channel<NoteResult>(Channel.CONFLATED).apply {
            var registration: ListenerRegistration? = null
            try {
                registration = getUserNotesCollection().addSnapshotListener { snapshot, exception ->
                    val value = exception?.let {
                        NoteResult.Error(it)
                    } ?: snapshot?.let {
                        NoteResult.Success(data = it.documents.map { documentSnapshot ->
                            documentSnapshot.toObject(
                                Note::class.java
                            )
                        })
                    }

                    value?.let { offer(it) }
                }
            } catch (exception: Throwable) {
                offer(NoteResult.Error(exception))
            }

            invokeOnClose { registration?.remove() }
        }

    override suspend fun getNoteById(id: String): Note =
        suspendCoroutine { continuation ->
            try {
                getUserNotesCollection().document(id).get()
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(snapshot.toObject(Note::class.java)!!)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            } catch (exception: Throwable) {
                continuation.resumeWithException(exception)
            }
        }

    override suspend fun saveNote(note: Note): Note =
        suspendCoroutine { continuation ->
            try {
                getUserNotesCollection().document(note.id).set(note)
                    .addOnSuccessListener {
                        Log.d(TAG, "Note $note is saved")
                        continuation.resume(note)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            } catch (exception: Throwable) {
                continuation.resumeWithException(exception)
            }
        }

    override suspend fun getCurrentUser(): User? =
        suspendCoroutine { continuation ->
            currentUser?.let {
                continuation.resume(
                    User(
                        name = it.displayName ?: "",
                        email = it.email ?: ""
                    )
                )
            } ?: continuation.resume(null)
        }

    override suspend fun deleteNote(id: String): Exception? =
        suspendCoroutine { continuation ->
            try {
                getUserNotesCollection().document(id).delete()
                    .addOnSuccessListener {
                        continuation.resume(null)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            } catch (exception: Throwable) {
                continuation.resumeWithException(exception)
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