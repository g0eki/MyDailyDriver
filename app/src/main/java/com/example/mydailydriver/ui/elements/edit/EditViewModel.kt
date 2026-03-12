package com.example.mydailydriver.ui.elements.edit

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydailydriver.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditViewModel(
    private val repository: NoteRepository,
    private val noteId: String? = null,
) : ViewModel() {

    // 1. Single Source of Truth für den UI State
    private val _uiState = MutableStateFlow(value=EditUiState())
    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()


    init {
        Log.d("DEBUG", "EditViewModel init, noteId: $noteId")
        if (noteId != null) {
            viewModelScope.launch {
                val note = repository.getNoteById(noteId)
                Log.d("DEBUG", "Note geladen: $note")

                if (note != null) {
                    // KORREKT: _uiState verwenden und immer .update nutzen
                    _uiState.update { currentState ->
                        currentState.copy(
                            title = note.title,
                            content = note.content,
                            isLoading = false
                        )
                    }
                } else {
                    // KORREKT: _uiState verwenden
                    _uiState.update { it.copy(isLoading = false) }
                }

            }
        }
    }

    // --- NEU: UDF Events für Texteingaben der UI ---
    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onContentChange(newContent: String) {
        _uiState.update { it.copy(content = newContent) }
    }
    // -----------------------------------------------


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