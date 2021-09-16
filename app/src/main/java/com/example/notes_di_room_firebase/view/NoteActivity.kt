package com.example.notes_di_room_firebase.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.notes_di_room_firebase.databinding.ActivityNoteBinding
import com.example.notes_di_room_firebase.model.Color
import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.viewmodel.NoteViewModel
import java.util.*

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    companion object {
        private const val EXTRA_NOTE = "NoteActivity.extra.note"

        fun getStartIntent(context: Context, noteId: String?): Intent =
            Intent(context, NoteActivity::class.java).putExtra(EXTRA_NOTE, noteId)
    }

    private var note: Note? = null
    override val ui: ActivityNoteBinding by lazy { ActivityNoteBinding.inflate(layoutInflater) }
    override val viewModel: NoteViewModel by lazy { ViewModelProvider(this).get(NoteViewModel::class.java) }
    private val textChangeListener = object : TextWatcher {
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

        val noteId = intent.getStringExtra(EXTRA_NOTE)

        setSupportActionBar(ui.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        noteId?.let {
            viewModel.loadNote(noteId = it)
        } ?: run {
            supportActionBar?.title = "New note"
            ui.toolbar.setBackgroundColor(Color.BLUE.getColorInt(this@NoteActivity))
        }

        ui.titleEt.addTextChangedListener(textChangeListener)
        ui.bodyEt.addTextChangedListener(textChangeListener)
    }

    private fun createNewNote(): Note = Note(
        id = UUID.randomUUID().toString(),
        title = ui.titleEt.text.toString(),
        body = ui.bodyEt.text.toString()
    )

    private fun initView() {
        note?.run {
            ui.toolbar.setBackgroundColor(color.getColorInt(this@NoteActivity))
            ui.titleEt.setText(title)
            ui.bodyEt.setText(body)
            supportActionBar?.title = lastChanged.format()
        }

        ui.titleEt.addTextChangedListener(textChangeListener)
        ui.bodyEt.addTextChangedListener(textChangeListener)
    }

    private fun triggerSaveNote() {
        if (ui.titleEt.text == null || ui.titleEt.text!!.length < 3) return

        Handler(Looper.getMainLooper()).postDelayed({
            note = note?.copy(
                title = ui.titleEt.text.toString(),
                body = ui.bodyEt.text.toString(),
                lastChanged = Date()
            ) ?: createNewNote()

            note?.let { note -> viewModel.saveChanges(note) }
        }, 2000L)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun renderData(data: Note?) {
        this.note = data
        initView()
    }
}