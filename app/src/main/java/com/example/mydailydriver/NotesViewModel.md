Gute Frage! Das kommt ins **ViewModel**. Lass mich das Schritt für Schritt zeigen:

---

## Die Architektur zuerst verstehen

```
NotesStore (Datenspeicher)
    ↑
NotesViewModel (Logik)
    ↑
NotesScreen / Activity (UI)
```

Jede Schicht kennt nur die Schicht **direkt unter ihr**.

---

## 1. NotesViewModel.kt

```kotlin
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val store: NotesStore  // Hilt liefert den Store automatisch!
) : ViewModel() {

    // UI beobachtet diese Liste
    val notes: StateFlow<List<Note>> = store.notes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Notiz hinzufügen
    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            store.addNote(title, content)
        }
    }

    // Notiz löschen
    fun deleteNote(id: String) {
        viewModelScope.launch {
            store.deleteNote(id)
        }
    }
}
```

---

## 2. NotesScreen.kt (Jetpack Compose)

```kotlin
@Composable
fun NotesScreen(
    viewModel: NotesViewModel = hiltViewModel()  // Hilt erstellt ViewModel automatisch!
) {
    // Notizen beobachten
    val notes by viewModel.notes.collectAsState()

    Column {
        // Alle Notizen anzeigen
        notes.forEach { note ->
            Text(text = note.title)
            Text(text = note.content)

            Button(onClick = { viewModel.deleteNote(note.id) }) {
                Text("Löschen")
            }
        }

        // Neue Notiz hinzufügen
        Button(onClick = { viewModel.addNote("Einkauf", "Milch, Eier, Brot") }) {
            Text("Notiz hinzufügen")
        }
    }
}
```

---

## Warum NICHT direkt in der Activity/Screen?

```kotlin
// ❌ FALSCH - niemals so!
class NotesActivity : AppCompatActivity() {
    val store = NotesStore(this)  // Problem: stirbt mit der Activity!
}
```

**Das Problem:** Wenn du das Handy drehst → Activity wird **zerstört und neu erstellt** → alle Daten weg!

Das ViewModel **überlebt** die Activity-Rotation – deshalb gehört die Logik dort rein. ✅

---

## Zusammenfassung: Wer macht was?

| Klasse | Aufgabe |
|---|---|
| `NotesStore` | Daten lesen/schreiben (DataStore) |
| `NotesViewModel` | Logik, `store` aufrufen, UI-State halten |
| `NotesScreen` | Nur anzeigen & Buttons, ruft ViewModel auf |

> **Faustregel:** Die UI fragt das ViewModel. Das ViewModel fragt den Store. Der Store redet mit DataStore. Nie überspringen! 🎯