package com.example.notes_di_room_firebase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notes_di_room_firebase.model.Repository
import com.example.notes_di_room_firebase.view.MainViewState

class MainViewModel : ViewModel() {

    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        viewStateLiveData.value = MainViewState(Repository.getNotes())
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData

}