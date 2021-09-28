package com.example.notes_di_room_firebase.viewmodel

import com.example.notes_di_room_firebase.model.NoAuthException
import com.example.notes_di_room_firebase.model.Repository
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: Repository) :
    BaseViewModel<Boolean?>() {

        fun requestUser() {
            launch {
                repository.getCurrentUser()?.let {
                    setData(true)
                } ?: setError(NoAuthException())
            }
        }

}