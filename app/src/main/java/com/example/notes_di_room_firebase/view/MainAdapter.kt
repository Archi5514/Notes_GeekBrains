package com.example.notes_di_room_firebase.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes_di_room_firebase.R
import com.example.notes_di_room_firebase.databinding.ItemNoteBinding
import com.example.notes_di_room_firebase.model.Color
import com.example.notes_di_room_firebase.model.Note

interface OnItemClickListener {
    fun onItemClick(note: Note)
}

class MainAdapter(private val onClickListener: OnItemClickListener) : RecyclerView.Adapter<MainAdapter.NoteViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) = holder.bind(notes[position])

    override fun getItemCount(): Int = notes.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ui: ItemNoteBinding = ItemNoteBinding.bind(itemView)

        fun bind(note: Note) {
            ui.title.text = note.title
            ui.body.text = note.body

            val color = when (note.color) {
                Color.WHITE -> R.color.color_white
                Color.BLUE -> R.color.color_blue
                Color.GREEN -> R.color.color_green
                Color.PINK -> R.color.color_pink
                Color.RED -> R.color.color_red
                Color.VIOLET -> R.color.color_violet
                Color.YELLOW -> R.color.color_yellow
                else -> R.color.color_white
            }

            itemView.setBackgroundColor(color)
            itemView.setOnClickListener {
                onClickListener.onItemClick(note)
            }
        }
    }

}