package com.example.mydailydriver.ui.elements.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydailydriver.data.repository.NoteRepository
import kotlinx.coroutines.launch

class EditViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    // Create and Delete
    fun addNote(newTitel: String, newNote: String) {
        viewModelScope.launch {
            // my_store_notes.addNote(title = newTitel, content = newNote)
            repository.addNote(title = newTitel, content = newNote)
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            repository.deleteNote(id = id)
        }
    }

    // setter
    fun updateNote(id: String, newTitel: String, newContent: String) {
        viewModelScope.launch {
            repository.updateNote(id = id, newTitle = newTitel, newContent = newContent)
        }
    }
}