package com.example.mydailydriver.ui.elements.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import com.example.mydailydriver.data.datastore.Note
import com.example.mydailydriver.ui.theme.MyDailyDriverTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mydailydriver.ui.elements.components.CustomTopBar

// ✅ Zustandsbehaftet – kennt das ViewModel
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel(),
               onAddNote: () -> Unit) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())
    HomeContent(notes = notes,
                onAddNote = onAddNote)
}

// ✅ Zustandslos – nur UI, previewbar!
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(notes: List<Note>,
                onAddNote: () -> Unit) {
    Scaffold(
        // topBar = { TopAppBar(title = { Text("My Daily Driver") }) },
        topBar = {
            CustomTopBar(titel = "My Daily Driver")
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {  Icon(Icons.Filled.Add, "Notiz hinzufügen") }
        }
    ) { innerPadding ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📝", style = MaterialTheme.typography.displayMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Keine Notizen vorhanden",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Füge deine erste Notiz hinzu!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(notes) { note -> NoteItem(note = note) }
            }
        }
    }
}

@Composable
fun NoteItem(note: Note) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
}

// ✅ Preview funktioniert jetzt – keine ViewModel-Abhängigkeit!
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyDailyDriverTheme {
        HomeContent(
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
fun HomeScreenEmptyPreview() {
    MyDailyDriverTheme {
        HomeContent(
            notes = emptyList(),
            onAddNote = {})  // ← Empty State testen!
    }
}