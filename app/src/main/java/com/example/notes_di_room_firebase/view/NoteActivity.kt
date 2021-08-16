package com.example.notes_di_room_firebase.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notes_di_room_firebase.R
import com.example.notes_di_room_firebase.databinding.ActivityNoteBinding
import com.example.notes_di_room_firebase.model.Color
import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.viewmodel.NoteViewModel
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
    private lateinit var viewModel: NoteViewModel
    private val textChangeListener = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            triggerSaveNote()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // suck dick
        }

        override fun afterTextChanged(s: Editable?) {
            // suck black dick
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(ui.root)

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

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

    private fun createNewNote(): Note = Note(
        id = UUID.randomUUID().toString(),
        title = ui.titleEt.text.toString(),
        body = ui.bodyEt.text.toString()
    )

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
        ui.titleEt.addTextChangedListener(textChangeListener)
        ui.bodyEt.addTextChangedListener(textChangeListener)
    }

    private fun triggerSaveNote() {
        if(ui.titleEt.text == null || ui.titleEt.text!!.length < 3) return

        runOnUiThread {
            note = note?.copy(
                title = ui.titleEt.text.toString(),
                body = ui.bodyEt.text.toString(),
                lastChanged = Date()
            ) ?: createNewNote()

            note?.let{ note -> viewModel.saveChanges(note) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}