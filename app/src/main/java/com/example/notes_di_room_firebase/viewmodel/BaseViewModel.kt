package com.example.notes_di_room_firebase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notes_di_room_firebase.view.BaseViewState

abstract class BaseViewModel<T, VS : BaseViewState<T>> : ViewModel() {

    val viewStateLiveData = MutableLiveData<VS>()

    fun getViewState(): LiveData<VS> = viewStateLiveData
}