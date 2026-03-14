package com.example.mydailydriver.ui.elements.navigation


import com.example.mydailydriver.R
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.mydailydriver.ui.elements.components.EditNote
import com.example.mydailydriver.ui.elements.components.Home
import com.example.mydailydriver.ui.elements.edit.EditScreen
import com.example.mydailydriver.ui.elements.components.Screens
import com.example.mydailydriver.ui.elements.home.HomeScreen
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.example.mydailydriver.ui.elements.components.NotesGroups


// 1. Kein @Composable hier!
internal class NavigationHelper(
    val navController: NavController
) {
    @Composable
    fun ModalDrawerSheetContent() {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.app_name),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Meine Notizen",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )


        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate(Screens.Start.name) }
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Edit, contentDescription = null) },
            label = { Text("Neue Notiz") },
            selected = false,
            onClick = { navController.navigate(Screens.Notes.name) }
        )
    }

/*    fun NavGraphBuilder.navHostContent() {
        composable(route = Screens.Start.name) {
            HomeScreen(
                onNavigateHome = { navController.navigate(route = Screens.Start.name) },
                onAddNote = { navController.navigate(route = Screens.Notes.name) }
            )
        }

        composable(route = Screens.Notes.name) {
            // val canGoBack = navController.previousBackStackEntry != null
            EditScreen(
                onBack = {
                    if (!navController.popBackStack()) {
                        navController.navigate(route = Screens.Start.name) {
                            popUpTo(0)
                        }
                    }
                },
                // onEditActions = barActions,
            )
        }
    }*/

    fun NavGraphBuilder.navHostContent() {
        composable<Home> {
            HomeScreen(
                onNavigateHome = { navController.navigate(route = Home) },
                onAddNote = { navController.navigate(route = EditNote()) },
                onEditNote = { note ->
                    navController.navigate(route = EditNote(noteId = note.id))
                },
                // onAddNoteGroup = { TODO() },
                // onAddNoteGroup = TODO(), // { navController.navigate(route = EditNoteGroup()) },
            )
        }

        composable<EditNote> {
            // val canGoBack = navController.previousBackStackEntry != null

            // /**/ toDO()
                 backStackEntry ->  // Lambda-Parameter
                 val editNote = backStackEntry.toRoute<EditNote>()

            // val editNote = it.toRoute<EditNote>() // ✅ Argument auslesen

            // val backStackEntry by navController.currentBackStackEntryAsState() ???


            EditScreen(

                onBack = {
                    if (!navController.popBackStack()) {
                            navController.navigate(route = Home) {
                                popUpTo<Home>() { inclusive = true} // Typensicheres Löschen des Backstacks
                        }
                    }
                },
                noteId = editNote.noteId
                // onEditActions = barActions,
            )
        }
        composable< NotesGroups> {
            TODO()

        }
    }
}

/*
TODO()
Du nutzt in deinem `NavGraphBuilder` bereits den absoluten **Cutting-Edge Standard** (Type-Safe Navigation, eingeführt in Navigation Compose 2.8.0) mit `composable<Home>`. Das ist hervorragend!

Allerdings vermischst du in deinem Code noch alte und neue Paradigmen, hast einen fatalen Klammer-Fehler eingebaut und nutzt ein Anti-Pattern für Jetpack Compose.

Hier ist die detaillierte Analyse deiner Fehler und wie du sie behebst:

### 1. Der Kompilierfehler: Falsche Klammernsetzung bei `popUpTo`

In deinem `EditScreen` Block hast du Folgendes geschrieben:

```kotlin
if (!navController.popBackStack()) {
    popUpTo(0) // FALSCH: Hängt in der Luft
    navController.navigate(route = Home) {
    }
}

```

**Warum das falsch ist:** `popUpTo` ist eine Funktion, die nur innerhalb des Konfigurations-Lambdas (Trailing Lambda) der `Maps`-Funktion existiert. Zudem ist `popUpTo(0)` die veraltete Methode. Beim Type-Safe-Ansatz übergibst du direkt die Klasse/das Objekt.
**Korrektur:**

```kotlin
if (!navController.popBackStack()) {
    navController.navigate(route = Home) {
        popUpTo<Home>() // Typensicheres Löschen des Backstacks
    }
}

```

### 2. Der Laufzeit-Crash: Mix aus Strings und Type-Safety

In deinen `NavigationDrawerItem`s rufst du die alte, String-basierte Navigation auf:
`onClick = { navController.navigate(Screens.Start.name) }`
**Warum das knallt:** Wenn dein Graph mit typensicheren Objekten (`composable<Home>`) aufgebaut ist, kennt der `NavController` die Route "Start" (als String) nicht. Er erwartet das Objekt `Home`. Sobald du auf das Item klickst, stürzt die App mit einer `IllegalArgumentException` ab.
**Korrektur:** `onClick = { navController.navigate(Home) }`

### 3. Architektur-Fehler: Composables in einer normalen Klasse

Du hast alles in eine `internal class NavigationHelper` gepackt. Das ist in Jetpack Compose ein absolutes Anti-Pattern.
**Warum das falsch ist:** UI-Komponenten in Compose sollten reine Funktionen (`@Composable fun`) sein. Wenn du sie an eine Instanz einer Klasse bindest, stört das den Recomposition-Mechanismus von Compose, da Compose den Zustand der Instanz überwachen muss, statt nur die Parameter der Funktion.

---

### 🔍 Syntax-Analyse (Unterscheidung)

* **Kotlin-Ebene (Trailing Lambdas & Scope):** Das Problem mit `popUpTo` war ein reiner Kotlin-Scope-Fehler. Du hast versucht, eine Funktion im äußeren Scope aufzurufen, die als *Receiver* (Empfänger) den `NavOptionsBuilder` benötigt, welcher nur innerhalb der geschweiften Klammern von `Maps { ... }` zur Verfügung steht. Das `it` bei `val editNote = it.toRoute<EditNote>()` ist ebenfalls reines Kotlin (impliziter Parameter für das Lambda).
* **Compose-Framework (Recomposition & Navigation):** Der Typensichere Navigation-Graph (`composable<...>`) wandelt Kotlin Serialization-Objekte tief im Compose-Framework in Routen um. Das Framework verlangt strikt, dass Navigation-Zustände (wie der `NavController`) an flache `@Composable` Funktionen übergeben (State Hoisting) und nicht in Objekthierarchien (wie deiner Helper-Klasse) eingesperrt werden.

---

### Die saubere, moderne Lösung (UDF & Type-Safe)

Löse dich von der `NavigationHelper`-Klasse. Teile es stattdessen in saubere, funktionale Composables auf. Wir gehen davon aus, dass deine Routen als `@Serializable` markiert sind (z. B. `@Serializable data object Home`).

```kotlin
package com.example.mydailydriver.ui.elements.navigation

// ... imports ...
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.navigation.NavOptionsBuilder

// 1. Reine Composable Funktion statt Klasse!
@Composable
fun DrawerContent(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.app_name),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Meine Notizen",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") },
            selected = false,
            // KORREKT: Type-Safe Navigation
            onClick = { navController.navigate(Home) }
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Edit, contentDescription = null) },
            label = { Text("Neue Notiz") },
            selected = false,
            // KORREKT: Type-Safe Navigation aufrufen
            onClick = { navController.navigate(EditNote()) }
        )
    }
}

// 2. Extension Function für den Graphen bleibt, aber nicht in einer Klasse!
fun NavGraphBuilder.setupNavGraph(navController: NavController) {

    composable<Home> {
        HomeScreen(
            onNavigateHome = { navController.navigate(route = Home) },
            onAddNote = { navController.navigate(route = EditNote()) },
            onEditNote = { note ->
                navController.navigate(route = EditNote(noteId = note.id))
            },
        )
    }

    composable<EditNote> { backStackEntry ->
        // KORREKT: Argument typensicher auslesen
        val editNote = backStackEntry.toRoute<EditNote>()

        EditScreen(
            noteId = editNote.noteId,
            onBack = {
                if (!navController.popBackStack()) {
                    navController.navigate(route = Home) {
                        // KORREKT: popUpTo innerhalb des Lambdas
                        popUpTo<Home>() { inclusive = true }
                    }
                }
            }
        )
    }
}

```

---

### 📚 Verifizierung & Dokumentation

Diese Implementierung folgt den neuesten Architekturrichtlinien für die Navigation in Compose mit Type-Safety.

* **Offizielle Doku:** [developer.android.com - Type safety in Navigation Compose](https://www.google.com/search?q=https://developer.android.com/guide/navigation/design/type-safe%3Fhl%3Dde)
* **Zitat (für STRG+F):** *"Navigation Compose version 2.8.0 and higher supports type safety for Kotlin DSL. ... you provide a route as a strongly typed object"*

Möchtest du, dass wir uns ansehen, wie du die `@Serializable` Klassen (Routen) für `Home` und `EditNote` perfekt aufbaust, damit die Argumenten-Übergabe (`noteId`) sicher funktioniert?
 */