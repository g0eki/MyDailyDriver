package com.example.mydailydriver.ui.elements.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mydailydriver.data.models.Note
import com.example.mydailydriver.data.models.NoteGroup

@Composable
fun AppScaffold(
    // topBar: @Composable (onMenuClick: () -> Unit) -> Unit = {},
    topBar:  @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { topBar () },
        floatingActionButton = { floatingActionButton() }
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
fun AppScaffold(
    topBar: @Composable (onMenuClick: () -> Unit) -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    notes: List<Note>,
    onEditNote: (Note) -> Unit = {},
) {
    Scaffold(
        topBar = { topBar {} },
        floatingActionButton = { floatingActionButton() }
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