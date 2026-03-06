//package com.example.mydailydriver.ui.elements
//
//class EditScreen {
//}

Das Problem ist dass `NoteScreen` jetzt Parameter braucht, aber im Preview kannst du kein echtes ViewModel übergeben.

---

**Lösung – Zustand von ViewModel trennen:**

```kotlin
@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    MyDailyDriverTheme {
        NoteScreen(
            viewModel = viewModel(),  // ❌ geht nicht im Preview!
            onNoteSaved = {}
        )
    }
}
```

Das funktioniert nicht. Deshalb macht man es so:

---

**Zustandslos/Zustandsbehaftet trennen** – du hattest das sogar schon als Kommentar:

```kotlin
// Zustandsbehaftet (hält State)
@Composable
fun NoteScreen(viewModel: MyDailyDriverViewModel, onNoteSaved: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    NoteScreenContent(
        title = title,
        body = body,
        onTitleChange = { title = it },
        onBodyChange = { body = it },
        onSave = { viewModel.addNote(title, body); onNoteSaved() },
        onBack = { onNoteSaved() }
    )
}

// Zustandslos (nur UI, previewbar!)
@Composable
fun NoteScreenContent(
    title: String,
    body: String,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    // dein ganzer Scaffold Code hier
}

// Preview ✅
@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    MyDailyDriverTheme {
        NoteScreenContent(
            title = "Test Titel",
            body = "Test Inhalt",
            onTitleChange = {},
            onBodyChange = {},
            onSave = {},
            onBack = {}
        )
    }
}
```

Das ist das **State Hoisting** Pattern – der echte Grund warum du den Kommentar bereits hattest! 🎯