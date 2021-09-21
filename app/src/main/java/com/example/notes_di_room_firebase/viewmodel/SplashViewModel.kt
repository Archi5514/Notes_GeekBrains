package com.example.notes_di_room_firebase.viewmodel

import com.example.notes_di_room_firebase.model.NoAuthException
import com.example.notes_di_room_firebase.model.Repository
import com.example.notes_di_room_firebase.view.SplashViewState

class SplashViewModel(private val repository: Repository) :
    BaseViewModel<Boolean?, SplashViewState>() {

        fun requestUser() {
            repository.getCurrentUser().observeForever { user ->
                viewStateLiveData.value = user?.let {
                    SplashViewState(isAuth = true)
                } ?: SplashViewState(error = NoAuthException())
            }
        }

}