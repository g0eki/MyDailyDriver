package com.example.mydailydriver.ui.elements.home

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import com.example.mydailydriver.data.models.Note
import com.example.mydailydriver.ui.theme.MyDailyDriverTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mydailydriver.data.models.NoteGroup
import com.example.mydailydriver.ui.AppViewModelProvider
import com.example.mydailydriver.ui.elements.components.AppDrawerContent
import com.example.mydailydriver.ui.elements.components.AppScaffold
import com.example.mydailydriver.ui.elements.components.CustomTopBar
import kotlinx.coroutines.launch


// toDO: siehe: ./HomeScreen_toDO.md

// ✅ Zustandsbehaftet – kennt das ViewModel
@Composable
fun HomeScreen(
    // Wir übergeben 'factory = HomeViewModelFactory'.
    // Dadurch wird das ViewModel exakt so gebaut, wie wir es unten in der Datei definiert haben.
    // NEW: siehe TODO(): AppViewModelProvider.kt
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider),
    onNavigateHome: () -> Unit = {},
    onAddNote: () -> Unit,
    onEditNote: (Note) -> Unit = {},
    onAddNoteGroup: () -> Unit,
    onSelectGroup: (NoteGroup) -> Unit = {},  // ✅ NEU
) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())
    val noteGroups by viewModel.noteGroups.collectAsState(initial = emptyList())


    HomeContent(
        notes = notes,
        noteGroups = noteGroups,  // ✅ NEU
        onNavigateHome = onNavigateHome,
        onAddNote = onAddNote,
        onEditNote = onEditNote,
        onAddNoteGroup = onAddNoteGroup,
        onSelectGroup = onSelectGroup,  // ✅ NEU
    )
}

// ✅ Zustandslos – nur UI, previewbar!
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    notes: List<Note>,
    noteGroups: List<NoteGroup> = emptyList(),  // ✅ NEU
    onNavigateHome: () -> Unit = {},
    onAddNote: () -> Unit,
    onEditNote: (Note) -> Unit = {},
    onAddNoteGroup: () -> Unit = {},
    onSelectGroup: (NoteGroup) -> Unit = {},  // ✅ NEU
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Falls du den Drawer per Button öffnen willst, brauchst du ein CoroutineScope
    // TODO() val scope = rememberCoroutineScope()
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                onNavigateHome = onNavigateHome,
                onAddNotesGroups = onAddNoteGroup,
                noteGroups = noteGroups,          // ✅
                onSelectGroup = onSelectGroup     // ✅
            )
        },
        // modifier = TODO(),
        // gesturesEnabled = TODO(),
        // scrimColor = TODO(),
    ) {
        //toDO: All-in-One:
        AppScaffold(
            topBar = {
                CustomTopBar(
                    titel = "My Daily Driver",
                    onMenuClick={
                        scope.launch {
                            // Öffnet den Drawer, falls zu, schließt ihn, falls offen
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()

                            // ODER alternativ, falls du nur öffnen willst:
                            // drawerState.apply { if (isClosed) open() else close() }
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onAddNote) {
                    Icon(
                        Icons.Filled.Add,
                        "Notiz hinzufügen"
                    )
                }
            },
            notes = notes,
            onEditNote = onEditNote,
        )

        //toDO: Partiel:
/*        AppScaffold(
            topBar = {
                CustomTopBar(
                    titel = "My Daily Driver",
                    onMenuClick={
                        scope.launch {
                            // Öffnet den Drawer, falls zu, schließt ihn, falls offen
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()

                            // ODER alternativ, falls du nur öffnen willst:
                            // drawerState.apply { if (isClosed) open() else close() }
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onAddNote) {
                    Icon(
                        Icons.Filled.Add,
                        "Notiz hinzufügen"
                    )
                }
            },
        ) {innerPadding ->
            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📝", style = MaterialTheme.typography.displayMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Keine Notizen vorhanden",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Füge deine erste Notiz hinzu!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(notes) { note ->
                        NoteItem(
                            note = note,
                            onClick = {onEditNote(note)},
                        )
                    }
                }
            }
        }*/
        //toDO: alter-Variante:
        /*
        Scaffold(
            // topBar = { TopAppBar(title = { Text("My Daily Driver") }) },
            topBar = {
                CustomTopBar(
                    titel = "My Daily Driver",
                    onMenuClick={
                        scope.launch {
                            // Öffnet den Drawer, falls zu, schließt ihn, falls offen
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()

                            // ODER alternativ, falls du nur öffnen willst:
                            // drawerState.apply { if (isClosed) open() else close() }
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onAddNote) {
                    Icon(
                        Icons.Filled.Add,
                        "Notiz hinzufügen"
                    )
                }
            }
        ) { innerPadding ->
            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📝", style = MaterialTheme.typography.displayMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Keine Notizen vorhanden",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Füge deine erste Notiz hinzu!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(notes) { note ->
                        NoteItem(
                            note = note,
                            onClick = {onEditNote(note)},
                            )
                    }
                }
            }
        } */
    }
}



// ✅ Preview funktioniert jetzt – keine ViewModel-Abhängigkeit!
@Preview(showBackground = true, showSystemUi = false)
@Composable
fun HomeScreenPreview() {
    MyDailyDriverTheme {
        HomeContent(
            notes = listOf(
                Note(title = "Einkauf", content = "Milch, Brot, Eier"),
                Note(title = "Meeting", content = "Montag 10 Uhr")
            ),
            onAddNote = {},
            onEditNote = {},
            onAddNoteGroup = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenEmptyPreview() {
    MyDailyDriverTheme {
        HomeContent(
            notes = emptyList(),
            onAddNote = {},
            onEditNote = {},
            onAddNoteGroup = {},
        )  // ← Empty State testen!
    }
}