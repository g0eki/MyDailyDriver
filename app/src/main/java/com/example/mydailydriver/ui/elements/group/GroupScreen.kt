package com.example.mydailydriver.ui.elements.group

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import com.example.mydailydriver.data.models.Note
import com.example.mydailydriver.ui.theme.MyDailyDriverTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mydailydriver.R
import com.example.mydailydriver.ui.AppViewModelProvider
import com.example.mydailydriver.ui.elements.components.CustomTopBar
import com.example.mydailydriver.ui.elements.components.NoteItem
import com.example.mydailydriver.ui.elements.home.HomeViewModel
import kotlinx.coroutines.launch


// toDO: siehe: ./HomeScreen_toDO.md

// ✅ Zustandsbehaftet – kennt das ViewModel
@Composable
fun GroupScreen(
    // Wir übergeben 'factory = HomeViewModelFactory'.
    // Dadurch wird das ViewModel exakt so gebaut, wie wir es unten in der Datei definiert haben.
    // NEW: siehe TODO(): AppViewModelProvider.kt
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider),
    onNavigateHome: () -> Unit = {},
    onAddNote: () -> Unit,
    onEditNote: (Note) -> Unit = {},  // NEU
    onAddNoteGroup: Nothing = TODO(),  // NEU
) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())
    GroupContent(
        notes = notes,
        onNavigateHome = onNavigateHome,
        onAddNote = onAddNote,
        onEditNote = onEditNote,
    )
}

// ✅ Zustandslos – nur UI, previewbar!
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupContent(
    notes: List<Note>,
    onNavigateHome: () -> Unit = {},
    onAddNote: () -> Unit,
    onEditNote: (Note) -> Unit = {}, // ✅
    ) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)


    // Falls du den Drawer per Button öffnen willst, brauchst du ein CoroutineScope
    // TODO() val scope = rememberCoroutineScope()
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.primaryContainer,
                drawerShape = RoundedCornerShape(16.dp),
                drawerTonalElevation = 12.dp,
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { onNavigateHome() }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Meine Notizen (Group)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    label = { Text("Neue Notiz") },
                    selected = false,
                    onClick = { onAddNote() }
                )
            }
        },
        // modifier = TODO(),
        // gesturesEnabled = TODO(),
        // scrimColor = TODO(),
    ) {
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
        }
    }
}

/*@Composable
fun NoteItem(note: Note, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick // toDO() - Rückverfolgen KArten anklicken ?
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(note.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
            if (note.content.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis)
            }
        }
    }
}*/

// ✅ Preview funktioniert jetzt – keine ViewModel-Abhängigkeit!
@Preview(showBackground = true, showSystemUi = false)
@Composable
fun GroupScreenPreview() {
    MyDailyDriverTheme {
        GroupContent(
            notes = listOf(
                Note(title = "Einkauf", content = "Milch, Brot, Eier"),
                Note(title = "Meeting", content = "Montag 10 Uhr")
            ),
            onAddNote = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GroupScreenEmptyPreview() {
    MyDailyDriverTheme {
        GroupContent(
            notes = emptyList(),
            onAddNote = {}
        )  // ← Empty State testen!
    }
}