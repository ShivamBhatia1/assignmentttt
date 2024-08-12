package com.example.assignmenttt



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView




import android.widget.ImageView
import android.widget.PopupMenu





class NotesAdapter(
    private val notes: List<Note>,
    private val onEditNote: (Note) -> Unit,
    private val onDeleteNote: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        holder.optionsImageView.setOnClickListener { view ->
            showPopupMenu(view, note)
        }
    }

    override fun getItemCount() = notes.size

    private fun showPopupMenu(view: View, note: Note) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.note_options_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_edit -> {
                    onEditNote(note)
                    true
                }
                R.id.action_delete -> {
                    onDeleteNote(note)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.note_title)
        val contentTextView: TextView = itemView.findViewById(R.id.note_content)
        val optionsImageView: ImageView = itemView.findViewById(R.id.note_options)
    }
}


