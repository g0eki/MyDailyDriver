package com.example.mydailydriver.data.models

import java.util.UUID
import kotlinx.serialization.Serializable



// Datenklasse für eine Notiz
/**
 * Das Basis-Element: Eine einzelne Notiz.
 */
@Serializable  // ← Das braucht kotlinx-serialization!
data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String
)

/**
 * Die Gruppe: Hält eine Liste von Notizen.
 * Später in Room wäre dies eine eigene Tabelle.
 */
@Serializable
data class NoteGroup(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val notes: List<Note> = emptyList()
)

/**
 * Optional: Falls später verschiedene Typen (Notizen, Checklisten)
 * in einem "Board" mischen
 */
// TODO(): Noch überlegen, wie sinnvoll es ist bzw. Nutzbar ?
@Serializable
sealed class BoardItem {
    @Serializable
    data class TextNote(val note: Note) : BoardItem()

    @Serializable
    data class TodoList(val title: String, val items: List<String>) : BoardItem()

    @Serializable
    data class ImageNote(val url: String, val caption: String) : BoardItem()
}
// TODO(): Noch überlegen, wie sinnvoll es ist bzw. Nutzbar ?
@Serializable
data class ProjectGroup(
    val title: String,
    val items: List<BoardItem> // Hier kann jetzt alles bunte gemischt drin liegen
)