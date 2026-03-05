package com.example.mydailydriver.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydailydriver.data.datastore.Note
import com.example.mydailydriver.data.datastore.NotesStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MyDailyDriverViewModel(application: Application) : AndroidViewModel(application) {

    private val my_store_notes = NotesStore(application)

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
    val note: Flow<List<Note>>
        get() = my_store_notes.notes

    // setter
    fun updateNote(id: String, newTitel: String, newContent: String) {
        viewModelScope.launch {
            my_store_notes.updateNote(id = id, newTitle = newTitel, newContent = newContent)
        }
    }
}