package com.example.mydailydriver.ui.elements.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mydailydriver.data.models.Note
import com.example.mydailydriver.data.datastore.NotesStore
import com.example.mydailydriver.data.models.NoteGroup
import com.example.mydailydriver.data.repository.NoteRepository
import com.example.mydailydriver.data.repository.NoteRepositoryImpl
import com.example.mydailydriver.ui.elements.home.HomeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroupViewModel(
    private val repository: NoteRepository,
    private val groupId: String  // ✅ NEU
): ViewModel() {

    private val _uiState = MutableStateFlow(GroupUiState())
    val uiState: StateFlow<GroupUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val group = repository.getNoteGroupById(groupId)
            if (group != null) {
                _uiState.update {
                    it.copy(
                        selectedGroup = group,
                        notesInGroup = group.notes
                    )
                }
            }
        }
    }

    fun selectGroup(group: NoteGroup) {
        _uiState.update { it.copy(
            selectedGroup = group,
            notesInGroup = group.notes
        )}
    }



    fun selectGroupById(id: String) {
        viewModelScope.launch {
            val group = repository.getNoteGroupById(id)
            if (group != null) {
                _uiState.update {
                    it.copy(
                        selectedGroup = group,
                        notesInGroup = group.notes
                    )
                }
            }
        }
    }

    /************************** ***************************/

    val notes: Flow<List<Note>> = repository.allNotes
    val noteGroup: Flow<List<NoteGroup>> = repository.allNoteGroups

    // Funktion zum Hinzufügen einer Notiz.
    // Wir nutzen den viewModelScope, weil Datenbank-Operationen asynchron (suspend)
    // ablaufen müssen, damit die App nicht einfriert.
    fun addNoteGroup(name: String, description: String) {
        viewModelScope.launch {
            // Wir sagen nur dem Repository: "Speicher das!". Wie das passiert, ist dem ViewModel egal.
            repository.addNoteToGroup(name = name, description = description)
        }
    }
}
