package com.example.notes_di_room_firebase.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

private const val NOTES_COLLECTION = "notes"

class FireStoreProvider : RemoteDataProvider {

    companion object {
        private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    }

    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.addSnapshotListener { snapshots, exception ->
            if(exception != null) {
                result.value = NoteResult.Error(error = exception)
            } else if(snapshots != null) {
                val notes = mutableListOf<Note>()

                for(snapshot: QueryDocumentSnapshot in snapshots) {
                    notes.add(snapshot.toObject(Note::class.java))
                }

                result.value = NoteResult.Success(data = notes)
            }
        }

        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(id).get()
            .addOnSuccessListener { snapshot ->
                result.value = NoteResult.Success(snapshot.toObject(Note::class.java))
            }
            .addOnFailureListener { exception ->
                result.value = NoteResult.Error(error = exception)
            }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(note.id).set(note)
            .addOnSuccessListener {
                Log.d(TAG, "Note $note is saved")
                result.value = NoteResult.Success(data = note)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error saving note $note, message: ${exception.message}")
                result.value = NoteResult.Error(error = exception)
            }

        return result
    }
}