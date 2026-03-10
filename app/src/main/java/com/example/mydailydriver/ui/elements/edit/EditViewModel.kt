package com.example.mydailydriver.ui.elements.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydailydriver.data.datastore.NotesStore
import com.example.mydailydriver.data.repository.NoteRepository
import kotlinx.coroutines.launch

class EditViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    // private val my_store_notes = NotesStore(application)

    // Create and Delete
    fun addNote(newTitel: String, newNote: String) {
        viewModelScope.launch {
            my_store_notes.addNote(title = newTitel, content = newNote)
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            my_store_notes.deleteNote(id = id)
        }
    }
    // getter
//    val note: Flow<List<Note>>
//        get() = my_store_notes.notes

    // setter
    fun updateNote(id: String, newTitel: String, newContent: String) {
        viewModelScope.launch {
            my_store_notes.updateNote(id = id, newTitle = newTitel, newContent = newContent)
        }
    }
}