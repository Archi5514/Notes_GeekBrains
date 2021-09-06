package com.example.notes_di_room_firebase.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.notes_di_room_firebase.R
import com.example.notes_di_room_firebase.viewmodel.BaseViewModel
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<T, VS : BaseViewState<T>> : AppCompatActivity() {

    abstract val viewModel: BaseViewModel<T, VS>
    abstract val ui: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ui.root)

        viewModel.getViewState().observe(this) {
            it?.apply {
                data?.let { data -> renderData(data = data) }
                error?.let { error -> renderError(error = error) }
            }
        }
    }

    abstract fun renderData(data: T)

    protected open fun renderError(error: Throwable) {
        error.message?.let { message ->
            //TODO
        }
    }
}