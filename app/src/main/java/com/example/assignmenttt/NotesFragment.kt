package com.example.assignmenttt

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class NotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: NotesAdapter
    private val notes = mutableListOf<Note>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NotesAdapter(notes, ::editNote, ::deleteNote)
        recyclerView.adapter = adapter

        loadNotes()

        val addButton: Button = view.findViewById(R.id.add_button)
        addButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddNoteFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun loadNotes() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("notes")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                notes.clear() // Clear the list first
                for (document in documents) {
                    val note = document.toObject(Note::class.java).copy(id = document.id)
                    notes.add(note)
                }
                adapter.notifyDataSetChanged() // Notify adapter of changes
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to load notes", exception)
            }
    }

    private fun editNote(note: Note) {
        val bundle = Bundle().apply {
            putParcelable("note", note)
        }
        val addNoteFragment = AddNoteFragment().apply {
            arguments = bundle
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, addNoteFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun deleteNote(note: Note) {
        note.id?.let { id ->
            firestore.collection("notes").document(id).delete()


            notes.removeIf { it.id == id }
            adapter.notifyDataSetChanged()


        }
    }

    companion object {
        private const val TAG = "NotesFragment"
    }
}


