package com.example.mydailydriver.ui.elements.group

import com.example.mydailydriver.data.models.NoteGroup
import com.example.mydailydriver.data.models.Note

data class GroupUiState(
    val selectedGroup: NoteGroup? = null,
    val notesInGroup: List<Note> = emptyList()
)