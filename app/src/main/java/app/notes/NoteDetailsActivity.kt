package app.notes

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.notes.databinding.ActivityNoteDetailsBinding
import app.notes.models.Note
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

class NoteDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityNoteDetailsBinding
    var title: String? = null
    var content:String? = null
    var docId:String? = null
    var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = intent.getStringExtra("title")
        content = intent.getStringExtra("content")
        docId = intent.getStringExtra("docId")

        if (docId != null && !docId!!.isEmpty()) {
            isEditMode = true
        }

        binding.notesTitleText.setText(title)
        binding.notesContentText.setText(content)
        if (isEditMode) {
            binding.pageTitle.setText("Edit Your Note")
            binding.deleteNoteTextviewBtn.setVisibility(View.VISIBLE)
        }

        binding.saveNoteBtn.setOnClickListener { saveNote() }
        binding.deleteNoteTextviewBtn.setOnClickListener { deleteNoteFromFirebase() }
    }

    fun saveNote() {
        val noteTitle = binding.notesTitleText.text.toString()
        val noteContent = binding.notesContentText.text.toString()

        if (noteTitle == null || noteTitle.isEmpty()) {
            binding.notesTitleText.error = "Title is required"
            return
        }

        val note = Note(noteTitle,noteContent, Timestamp.now())
        saveNoteToFirebase(note)
    }

    fun saveNoteToFirebase(note: Note?) {
        val documentReference: DocumentReference
        documentReference = if (isEditMode) {
            Utility.getCollectionReferenceForNotes()!!.document(docId!!)
        } else {
            Utility.getCollectionReferenceForNotes()!!.document()
        }
        documentReference.set(note!!).addOnCompleteListener {
            if (it.isSuccessful) {
                Utility.showToast(this, "Note added successfully")
            } else {
                Utility.showToast(this, "Failed while adding note")
            }
        }
    }

    fun deleteNoteFromFirebase() {
        val documentReference: DocumentReference
        documentReference = Utility.getCollectionReferenceForNotes()!!.document(docId!!)
        documentReference.delete().addOnCompleteListener {
            if (it.isSuccessful) {
                Utility.showToast(this, "Note deleted successfully")
            } else {
                Utility.showToast(this, "Failed while deleting note")
            }
        }
    }
}