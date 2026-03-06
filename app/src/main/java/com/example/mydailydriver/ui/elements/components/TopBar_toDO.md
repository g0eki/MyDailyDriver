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


# --- 5

Gar kein Problem! Das ist tatsächlich der beste und sauberste Weg, um flexibel mehrere Icons (Actions) in einer TopBar darzustellen.

Um mehrere Eigenschaften wie das Icon, die Beschreibung und die Klick-Aktion zu bündeln, erstellst du am besten eine kleine Datenklasse (`data class`).

Hier ist die Umsetzung:

### 1. Die Datenklasse erstellen

Diese Klasse hält alle Informationen, die ein einzelner Button in deiner TopBar braucht.

```kotlin
import androidx.compose.ui.graphics.vector.ImageVector

data class TopBarAction(
    val imageVector: ImageVector,
    val contentDescription: String?, // String? erlaubt null, falls ein Icon rein dekorativ ist
    val onClick: () -> Unit
)

```

---

### 2. Die CustomTopBar anpassen

Jetzt änderst du den Parameter in deiner TopBar so, dass er eine Liste dieser neuen Datenklasse erwartet. Ich habe den Parameternamen von `onSave` zu `actions` geändert, da das semantisch besser passt, wenn es verschiedene Icons sein können.

```kotlin
import androidx.compose.runtime.Composable

@Composable
fun CustomTopBar(
    onBack: () -> Unit,
    actions: List<TopBarAction>? = null  // Liste von Aktionen oder null
) {
    // Hier kommt dein TopBar-Code hin...
}

```

---

### 3. So rufst du die TopBar dann auf

Wenn du den Screen aufrufst, kannst du die Liste nun ganz bequem übergeben:

```kotlin
CustomTopBar(
    onBack = { /* Zurück navigieren */ },
    actions = listOf(
        TopBarAction(
            imageVector = Icons.Default.Edit,
            contentDescription = "Bearbeiten",
            onClick = { /* Bearbeiten Logik */ }
        ),
        TopBarAction(
            imageVector = Icons.Default.Check,
            contentDescription = "Speichern",
            onClick = { /* Speichern Logik */ }
        )
    )
)

```

Möchtest du, dass ich dir auch direkt zeige, wie du diese `actions`-Liste innerhalb des Composables (z. B. im `actions`-Block einer Material 3 `TopAppBar`) mit einer Schleife durchgehst und als klickbare `IconButton`s auf den Bildschirm zeichnest?