package app.notes.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.notes.NoteDetailsActivity
import app.notes.Utility
import app.notes.databinding.NoteItemBinding
import app.notes.models.Note
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class NoteAdapter(options: FirestoreRecyclerOptions<Note>):
    FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>(options) {
    class NoteViewHolder(private val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(note: Note, docId: String){
         binding.noteTitleTextview.text = note.title
         binding.noteContentTextview.text = note.content
         binding.noteTimestampTextview.text = Utility.timeStampToString(note.timestamp)

            val context = binding.root.context
            binding.root.setOnClickListener {
                val intent = Intent(context, NoteDetailsActivity::class.java)
                intent.putExtra("title", note.title)
                intent.putExtra("content", note.content)
                intent.putExtra("docId", docId)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, note: Note) {
        val docId: String = snapshots.getSnapshot(position).id
        holder.bindData(note, docId)
    }
}