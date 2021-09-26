package com.example.notes_di_room_firebase.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel<T> : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Default + SupervisorJob()
    }

    private val viewStateChannel = BroadcastChannel<T>(Channel.CONFLATED)
    private val errorChannel = Channel<Throwable>()

    open val viewStateLiveData = MutableLiveData<T>()

    open fun getViewState(): ReceiveChannel<T> = viewStateChannel.openSubscription()
    fun getErrorChannel(): Channel<Throwable> = errorChannel

    protected fun setError(error: Throwable) = runBlocking {
        launch {

        }
    }

}