package com.example.notes_di_room_firebase.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import com.example.notes_di_room_firebase.R
import com.example.notes_di_room_firebase.databinding.ActivityNoteBinding
import com.example.notes_di_room_firebase.model.Color
import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.viewmodel.NoteViewModel
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {

    private var note: Note? = null
    private var activityColor: Color = Color.BLUE
    override val ui: ActivityNoteBinding by lazy { ActivityNoteBinding.inflate(layoutInflater) }
    override val viewModel: NoteViewModel by viewModel()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        menuInflater.inflate(R.menu.menu_note, menu).let { true }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteId = intent.getStringExtra(EXTRA_NOTE)

        setSupportActionBar(ui.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        noteId?.let {
            viewModel.loadNote(noteId = it)
        } ?: run {
            supportActionBar?.title = "New note"
            ui.toolbar.setBackgroundColor(activityColor.getColorInt(this@NoteActivity))
        }

        setEditListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed().let { true }
            R.id.palette -> togglePalette().let { true }
            R.id.delete -> deleteNote().let { true }
            else -> super.onOptionsItemSelected(item)
        }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()

        this.note = data.note
        note?.let { activityColor = it.color }
        initView()
    }

    private fun setEditListener() {
        ui.titleEt.addTextChangedListener(textChangeListener)
        ui.bodyEt.addTextChangedListener(textChangeListener)
    }

    private fun removeEditListener() {
        ui.titleEt.removeTextChangedListener(textChangeListener)
        ui.bodyEt.removeTextChangedListener(textChangeListener)
    }

    private fun togglePalette() {
        val colorMenu = PopupMenu(this, ui.appbar)
        colorMenu.menuInflater.inflate(R.menu.menu_color, colorMenu.menu)

        colorMenu.setOnMenuItemClickListener {
            activityColor = when (it.itemId) {
                R.id.colorBlue -> Color.BLUE
                R.id.colorGreen -> Color.GREEN
                R.id.colorRed -> Color.RED
                R.id.colorViolet -> Color.VIOLET
                R.id.colorWhite -> Color.WHITE
                R.id.colorYellow -> Color.YELLOW
                R.id.colorPink -> Color.PINK
                else -> Color.BLUE
            }
            note?.let { note ->
                updateNote()
                viewModel.saveChanges(note)
            } ?: kotlin.run { note = createNewNote() }

            initView()

            return@setOnMenuItemClickListener true
        }

        colorMenu.show()
    }

    private fun deleteNote() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_note_title)
            .setMessage(R.string.delete_note_message)
            .setPositiveButton(R.string.ok_btn_title) { _, _ -> viewModel.deleteNote() }
            .setNegativeButton(R.string.logout_dialog_cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun createNewNote(): Note = Note(
        id = UUID.randomUUID().toString(),
        title = ui.titleEt.text.toString(),
        body = ui.bodyEt.text.toString(),
        color = activityColor
    )

    private fun initView() {
        note?.run {
            ui.toolbar.setBackgroundColor(color.getColorInt(this@NoteActivity))

            removeEditListener()
            if (title != ui.titleEt.text.toString()) {
                ui.titleEt.setText(title)
            }
            if (body != ui.bodyEt.text.toString()) {
                ui.bodyEt.setText(body)
            }
            setEditListener()

            supportActionBar?.title = lastChanged.format()
        }
    }

    private fun triggerSaveNote() {
        if (ui.titleEt.text == null || ui.titleEt.text!!.length < 3) return

        Handler(Looper.getMainLooper()).postDelayed({
            note?.let { updateNote() } ?: run { note = createNewNote() }

            note?.let { note -> viewModel.saveChanges(note) }
        }, 2000L)

    }

    private fun updateNote() {
        note = note?.copy(
            title = ui.titleEt.text.toString(),
            body = ui.bodyEt.text.toString(),
            lastChanged = Date(),
            color = activityColor
        )
    }

    companion object {
        private const val EXTRA_NOTE = "NoteActivity.extra.note"

        fun getStartIntent(context: Context, noteId: String?): Intent =
            Intent(context, NoteActivity::class.java).putExtra(EXTRA_NOTE, noteId)
    }

}