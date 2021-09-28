package com.example.notes_di_room_firebase.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.notes_di_room_firebase.R
import com.example.notes_di_room_firebase.viewmodel.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity<T> : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Main + Job()
    }

    private lateinit var dataJob: Job
    private lateinit var errorJob: Job

    abstract val viewModel: BaseViewModel<T>
    abstract val ui: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ui.root)
    }

    override fun onStart() {
        super.onStart()

        dataJob = launch {
            viewModel.getViewState().consumeEach {
                renderData(it)
            }
        }

        errorJob = launch {
            viewModel.getErrorChannel().consumeEach {
                renderError(it)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        dataJob.cancel()
        errorJob.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }

    abstract fun renderData(data: T)

    protected open fun renderError(error: Throwable) {
        error.message?.let { message ->
            Snackbar.make(ui.root, message, Snackbar.LENGTH_INDEFINITE).apply {
                setAction(R.string.snackbar_clear) { dismiss() }
                show()
            }
        }
    }
}