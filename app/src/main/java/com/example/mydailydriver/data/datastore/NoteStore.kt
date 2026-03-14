package com.example.mydailydriver.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mydailydriver.data.models.Note
import com.example.mydailydriver.data.models.NoteGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.first  // ✅ NEU

private val Context.notesDataStore by preferencesDataStore(name = "notes-store")
//TODO: JSON ist nur nötig, weil DataStore nur primitive Typen Speichern kann: [Int, String, etc.]
//TODO: Es ist eig. nur für Theme, Language gedacht.
//TODO: Langfristig, Room wechseln
/*
"notes-store" (Datei)
    ├── "notes"   → "[{id:..., title:...}]"      // List<Note>
    └── "groups"  → "[{id:..., name:...}]"        // List<NoteGroup>

## 1. Warum extra Key?

`preferencesDataStore` ist wie eine **HashMap** — eine Datei, viele Keys:

```
"notes-store" (Datei)
    ├── "notes"   → "[{id:..., title:...}]"      // List<Note>
    └── "groups"  → "[{id:..., name:...}]"        // List<NoteGroup>
```

Du kannst nicht `notes` für beides verwenden — weil:
```kotlin
// "notes" enthält List<Note> JSON
// du kannst es nicht als List<NoteGroup> deserialisieren! ❌
Json.decodeFromString<List<NoteGroup>>(json)  // Crash!
```

---

## 2. Was ist `notesDataStore`?

```kotlin
private val Context.notesDataStore by preferencesDataStore(name = "notes-store")
```

Das ist der **Dateiname** auf dem Gerät:

```
/data/data/com.example.app/files/datastore/notes-store.preferences_pb
```

Das ist die **Datei** — die Keys sind die **Einträge darin**:

```
Datei    = notes-store    (Schrank)
Keys     = "notes"        (Schublade 1)
           "groups"       (Schublade 2)
```

---

## Zusammenfassung:

| | Was ist es? |
|---|---|
| `notes-store` | Die Datei auf dem Gerät |
| `notesKey` | Ein Eintrag/Schlüssel in der Datei |
| `groupsKey` | Ein anderer Eintrag in derselben Datei |
 */


class NotesStore(private val context: Context) {

    // Alle Notizen als Flow
    val notes: Flow<List<Note>> = context.notesDataStore.data
        .map { preferences ->
            val json = preferences[notesKey] ?: "[]"
            Json.decodeFromString<List<Note>>(json)  // JSON → Liste
        }

    // Notiz hinzufügen
    suspend fun addNote(title: String, content: String) {
        context.notesDataStore.edit { preferences ->
            val json = preferences[notesKey] ?: "[]"
            val currentNotes = Json.decodeFromString<List<Note>>(json).toMutableList()
            currentNotes.add(Note(title = title, content = content))
            preferences[notesKey] = Json.encodeToString(currentNotes)  // Liste → JSON
        }
    }

    // Notiz löschen (per ID)
    suspend fun deleteNote(id: String) {
        context.notesDataStore.edit { preferences ->
            val json = preferences[notesKey] ?: "[]"
            val currentNotes = Json.decodeFromString<List<Note>>(json).toMutableList()
            currentNotes.removeIf { it.id == id }
            preferences[notesKey] = Json.encodeToString(currentNotes)
        }
    }

    // Notiz bearbeiten (per ID)
    suspend fun updateNote(id: String, newTitle: String, newContent: String) {
        context.notesDataStore.edit { preferences ->
            val json = preferences[notesKey] ?: "[]"
            val currentNotes = Json.decodeFromString<List<Note>>(json).toMutableList()
            val index = currentNotes.indexOfFirst { it.id == id }
            if (index != -1) {
                currentNotes[index] = currentNotes[index].copy(
                    title = newTitle,
                    content = newContent
                )
            }
            preferences[notesKey] = Json.encodeToString(currentNotes)
        }
    }

    suspend fun getNoteById(id: String): Note? {
        val json = context.notesDataStore.data.first()[notesKey] ?: "[]"
        return Json.decodeFromString<List<Note>>(json).find { it.id == id }
    }

    val notesGroup: Flow<List<NoteGroup>> = context.notesDataStore.data
        .map { preferences ->
            val json = preferences[groupsKey] ?: "[]"
            Json.decodeFromString<List<NoteGroup>>(json)
        }

    suspend fun addNoteGroup(name: String, description: String) {
        context.notesDataStore.edit { preferences ->
            val jsonNoteGroup = preferences[groupsKey] ?: "[]"
            val currentGroup = Json.decodeFromString<List<NoteGroup>>(jsonNoteGroup).toMutableList()

            currentGroup.add(NoteGroup(name = name, description = description))
            preferences[groupsKey] = Json.encodeToString(currentGroup)

        }
    }

    suspend fun deleteNoteGroup(id: String) {
        context.notesDataStore.edit { preferences ->
            val json = preferences[groupsKey] ?: "[]"
            val currentNoteGroup = Json.decodeFromString<List<NoteGroup>>(json).toMutableList()
            currentNoteGroup.removeIf { it.id == id }
            preferences[groupsKey] = Json.encodeToString(currentNoteGroup)
        }
    }

    suspend fun updateNoteGroup(idGroup: String, note: Note) {
        context.notesDataStore.edit { preferences ->
            val jsonGroup = preferences[groupsKey] ?: "[]"
            val currentGroup = Json.decodeFromString<List<NoteGroup>>(jsonGroup).toMutableList()
            val indexGroup = currentGroup.indexOfFirst { it.id == idGroup }
            if (indexGroup != -1) {
                val updatedNotes = currentGroup[indexGroup].notes + note // Hinzufügen der Notiz zur Gruppe
                currentGroup[indexGroup] = currentGroup[indexGroup].copy(notes = updatedNotes)
            }

        }
    }

    suspend fun getNoteGroupById(id: String): NoteGroup? {
        val json = context.notesDataStore.data.first()[groupsKey] ?: "[]"
        return Json.decodeFromString<List<NoteGroup>>(json).find { it.id == id }
    }

    companion object {
        private val notesKey = stringPreferencesKey("notes")
        private val groupsKey = stringPreferencesKey("groups")
    }
}