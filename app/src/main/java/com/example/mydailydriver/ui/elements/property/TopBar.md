In `NoteScreenContent` – da gehört die **gesamte UI** rein:

```kotlin
@Composable
fun NoteScreenContent(
    title: String,
    body: String,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onBack() }) { // 👈 Lambda von außen
                        Icon(...)
                    }
                },
                actions = {
                    TextButton(onClick = { onSave() }) { // 👈 Lambda von außen
                        Text("Speichern")
                    }
                }
            )
        }
    ) { innerPadding ->
        // BasicTextFields hier
    }
}
```

---

**Die Regel ist:**
- `NoteScreen` → hält **State + ViewModel** – keine UI
- `NoteScreenContent` → hat **gesamte UI** (Scaffold, TopBar, TextFields) – kein ViewModel, kein State

So ist die Trennung sauber und der Preview funktioniert! ✅



# ---22

Nein! Du kannst eine **gemeinsame TopBar** Composable erstellen:

---

```kotlin
@Composable
fun MyAppTopBar(
    onBack: () -> Unit,
    onSave: (() -> Unit)? = null  // optional, nicht jeder Screen hat Speichern
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Zurück"
                )
            }
        },
        actions = {
            if (onSave != null) {  // nur anzeigen wenn übergeben
                TextButton(onClick = { onSave() }) {
                    Text("Speichern", fontWeight = FontWeight.SemiBold)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            MaterialTheme.colorScheme.background
        )
    )
}
```

---

**Dann in jedem Screen einfach:**

```kotlin
// NoteScreen - mit Speichern
Scaffold(
    topBar = { MyAppTopBar(onBack = onBack, onSave = onSave) }
)

// AndererScreen - nur Zurück
Scaffold(
    topBar = { MyAppTopBar(onBack = onBack) }
)
```

---

**Wo speichern?**
Am besten in einer neuen Datei z.B. `CommonComponents.kt` oder `AppTopBar.kt` – Komponenten die **mehrere Screens teilen**. 🎯