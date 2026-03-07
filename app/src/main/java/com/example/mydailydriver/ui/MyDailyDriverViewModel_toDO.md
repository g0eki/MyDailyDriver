Das ist ein perfektes Beispiel, um von einem „God ViewModel“ (dein `MyDailyDriverViewModel`) auf die saubere „Ein ViewModel pro Screen“-Architektur umzusteigen!

Aktuell machst du genau das, wovon wir abgeraten haben: Du erstellst das `MyDailyDriverViewModel` ganz oben in der `Navigation.kt` und reichst es dann vermutlich durch deinen `NavigationHelper` an alle Screens weiter.

Hier ist die Schritt-für-Schritt-Anleitung, wie wir das in eine saubere UDF-Architektur umbauen.

### Schritt 1: Das globale ViewModel aufteilen

Wir löschen das `MyDailyDriverViewModel` komplett und erstellen stattdessen zwei spezifische ViewModels. Eines für den Home-Screen und eines für den Edit-Screen.
*(Hinweis: Da du aktuell `AndroidViewModel` nutzt, behalte ich das für den einfachen Übergang bei. Perspektivisch baut man das oft mit Dependency Injection wie Hilt oder einem `AppContainer` auf, was du in deinen Kommentaren schon angedeutet hast).*

**1. Neues `HomeViewModel.kt**`
Dieses ViewModel kümmert sich *nur* um das Lesen der Notizen für die Liste.

```kotlin
package com.example.mydailydriver.ui.elements.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mydailydriver.data.datastore.Note
import com.example.mydailydriver.data.datastore.NotesStore
import kotlinx.coroutines.flow.Flow

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val notesStore = NotesStore(application)

    // Getter für die Liste
    val notes: Flow<List<Note>> = notesStore.notes
}

```

**2. Neues `EditViewModel.kt**`
Dieses ViewModel kümmert sich *nur* um das Erstellen, Updaten und Löschen von Notizen.

```kotlin
package com.example.mydailydriver.ui.elements.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydailydriver.data.datastore.NotesStore
import kotlinx.coroutines.launch

class EditViewModel(application: Application) : AndroidViewModel(application) {
    
    private val notesStore = NotesStore(application)

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            notesStore.addNote(title = title, content = content)
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            notesStore.deleteNote(id = id)
        }
    }

    fun updateNote(id: String, newTitle: String, newContent: String) {
        viewModelScope.launch {
            notesStore.updateNote(id = id, newTitle = newTitle, newContent = newContent)
        }
    }
}

```

### Schritt 2: Die Screens anpassen (State Hoisting)

Jetzt binden wir die neuen ViewModels direkt auf Screen-Ebene ein. Die Funktion `viewModel()` (aus `androidx.lifecycle.viewmodel.compose.viewModel`) sorgt dafür, dass das ViewModel automatisch an den Lebenszyklus dieses speziellen Screens gebunden wird!

**1. `HomeScreen.kt` anpassen:**

```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel // WICHTIG!

// ✅ Zustandsbehaftet – holt sich SEIN EIGENES ViewModel
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(), // Hier passiert die Magie!
    onAddNote: () -> Unit
) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())
    
    HomeContent(
        notes = notes,
        onAddNote = onAddNote
    )
}
// HomeContent bleibt exakt so wie es ist! Es ist perfekt zustandslos.

```

**2. `EditScreen.kt` anpassen:**

```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel // WICHTIG!

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    viewModel: EditViewModel = viewModel(), // Sein eigenes ViewModel!
    onBack: (() -> Unit)? = null,
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val bodyFocusRequester = remember { FocusRequester() }

    val barActions = listOf(
        // ... Edit Action ...
        TopBarAction(
            imageVector = Icons.Default.Save,
            contentDescription = "Speichern",
            onClick = {
                // Hier wird das spezifische EditViewModel aufgerufen
                viewModel.addNote(title, content)
                onBack?.invoke() // Nach dem Speichern zurücknavigieren macht Sinn!
            }
        ),
    )

    EditContent(
        onBack = onBack,
        title = title,
        onTitelChange = { title = it },
        content = content,
        onContentChange = { content = it },
        barActions = barActions,
        bodyFocusRequester = bodyFocusRequester
    )
}
// EditContent bleibt exakt so wie es ist!

```

### Schritt 3: Die Navigation bereinigen

Der wichtigste Schritt: Wir entfernen das ViewModel aus der Navigationsebene! Der `NavHost` bzw. die Navigation darf sich nicht um ViewModels kümmern.

**`Navigation.kt` anpassen:**

```kotlin
package com.example.mydailydriver.ui.elements.navigation

// ... imports ...

@Composable
fun Navigation() { // KEIN ViewModel mehr als Parameter!
    val navController = rememberNavController()
    
    // Deinem NavigationHelper übergibst du KEIN ViewModel mehr.
    val nav = NavigationHelper(navController = navController)

    ModalNavigationDrawer(
        // ... Code bleibt gleich ...
    ) {
        NavHost(
            navController = navController,
            startDestination = Screens.Start.name,
            modifier = Modifier
        ) {
            with(nav) {
                navHostContent() 
            }
        }
    }
}

```

In deinem `NavigationHelper` (den ich hier nicht komplett sehe) rufst du dann in den `composable("route") { ... }` Blöcken einfach nur noch `HomeScreen(onAddNote = { ... })` oder `EditScreen(onBack = { ... })` auf. Du musst **kein** ViewModel mehr übergeben.

### Warum ist das jetzt so viel besser?

1. **Speicher:** Wenn du vom `EditScreen` zurück zum `HomeScreen` gehst, wird das `EditViewModel` sofort zerstört. Es verbraucht keinen RAM mehr im Hintergrund.
2. **Übersicht:** Wenn du in Zukunft 20 Screens hast, ist dein Code nicht in einer riesigen `MyDailyDriverViewModel`-Datei gefangen, sondern sauber auf 20 kleine, leicht verständliche Dateien aufgeteilt.
3. **Zustandlosigkeit:** Deine `...Content`-Composables (z.B. `HomeContent` und `EditContent`) sind perfekt gebaut. Du hast UI und Logik jetzt makellos getrennt!