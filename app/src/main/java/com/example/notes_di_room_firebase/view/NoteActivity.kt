package com.example.notes_di_room_firebase.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.notes_di_room_firebase.R
import com.example.notes_di_room_firebase.databinding.ActivityNoteBinding
import com.example.notes_di_room_firebase.model.Color
import com.example.notes_di_room_firebase.model.Note
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_NOTE = "NoteActivity.extra.note"

        fun getStartIntent(context: Context, note: Note?): Intent =
            Intent(context, NoteActivity::class.java).putExtra(EXTRA_NOTE, note)

    }

    private var note: Note? = null
    private lateinit var ui: ActivityNoteBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        ui = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(ui.root)

        note = intent.getParcelableExtra(EXTRA_NOTE)
        setSupportActionBar(ui.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (note != null) {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note!!.lastChanged)
        } else {
            getString(R.string.new_note_title)
        }

        initView()
    }

    private fun initView() {
        ui.titleEt.setText(note?.title ?: "")
        ui.bodyEt.setText(note?.body ?: "")

        val color = when (note?.color) {
            Color.WHITE -> R.color.color_white
            Color.BLUE -> R.color.color_blue
            Color.GREEN -> R.color.color_green
            Color.PINK -> R.color.color_pink
            Color.RED -> R.color.color_red
            Color.VIOLET -> R.color.color_violet
            Color.YELLOW -> R.color.color_yellow
            else -> R.color.color_white
        }

        ui.toolbar.setBackgroundColor(resources.getColor(color))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

}