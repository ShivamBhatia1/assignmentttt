package com.example.assignmenttt

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth


import com.google.firebase.firestore.FirebaseFirestore







class AddNoteFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private var noteToEdit: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_note, container, false)

        firestore = FirebaseFirestore.getInstance()


        val titleEditText: EditText = view.findViewById(R.id.title_edit_text)
        val contentEditText: EditText = view.findViewById(R.id.content_edit_text)
        val saveButton: Button = view.findViewById(R.id.save_button)

        noteToEdit = arguments?.getParcelable("note")

        noteToEdit?.let {
            titleEditText.setText(it.title)
            contentEditText.setText(it.content)
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (noteToEdit == null) {
                saveNote(title, content)
            } else {
                updateNote(noteToEdit!!.id!!, title, content)
            }
        }

        return view
    }

    private fun saveNote(title: String, content: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val newNote = Note(
            title = title,
            content = content,
            userId = userId
        )

        firestore.collection("notes")
            .add(newNote)
            .addOnSuccessListener {
                // Log success
                Log.d(TAG, "Note saved successfully, navigating to NotesFragment")
                // Navigate to notesfnt()
            }
            .addOnFailureListener {

                Log.e(TAG, "Failed to save note", it)
            }
        navigateToNotesFragment()
    }

    private fun updateNote(id: String, title: String, content: String) {
        val updatedNote = Note(
            id = id,
            title = title,
            content = content,
            userId = FirebaseAuth.getInstance().currentUser?.uid
        )

        firestore.collection("notes").document(id)
            .set(updatedNote)
            .addOnSuccessListener {
                Log.d(TAG, "Note updated successfully, navigating to NotesFragment")
                navigateToNotesFragment()
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to update note", it)
            }

    }




    private fun navigateToNotesFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, NotesFragment())
            .addToBackStack(null)
            .commit()
    }
    }








