package com.example.notes_di_room_firebase.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel<T> : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Default + Job()
    }

    private val viewStateChannel = BroadcastChannel<T>(Channel.CONFLATED)
    private val errorChannel = Channel<Throwable>()

    val viewStateLiveData = MutableLiveData<T>()

    open fun getViewState(): ReceiveChannel<T> = viewStateChannel.openSubscription()
    fun getErrorChannel(): Channel<Throwable> = errorChannel

    protected fun setData(data: T) {
        launch {
            viewStateChannel.send(data)
        }
    }

    protected fun setError(error: Throwable) {
        launch {
            errorChannel.send(error)
        }
    }

    override fun onCleared() {
        viewStateChannel.close()
        errorChannel.close()
        coroutineContext.cancel()

        super.onCleared()
    }

}