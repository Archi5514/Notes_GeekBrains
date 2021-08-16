package com.example.notes_di_room_firebase.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.notes_di_room_firebase.databinding.ActivityMainBinding
import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var ui: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private  lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        setSupportActionBar(ui.toolbar)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        adapter = MainAdapter(object: OnItemClickListener {
            override fun onItemClick(note: Note) {
                openNoteScreen(note = note)
            }
        })
        ui.mainRecycler.adapter = adapter

        viewModel.viewState().observe(this, { state ->
            state?.let { adapter.notes = state.notes }
        })

        ui.fab.setOnClickListener { openNoteScreen() }
    }

    private fun openNoteScreen(note: Note? = null) {
        startActivity(NoteActivity.getStartIntent(context = this, note = note))
    }
}