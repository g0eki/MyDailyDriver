package com.example.mydailydriver.ui.elements.group

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mydailydriver.data.models.Note
import com.example.mydailydriver.data.models.NoteGroup
import com.example.mydailydriver.ui.AppViewModelProvider
import com.example.mydailydriver.ui.elements.components.AppScaffold
import com.example.mydailydriver.ui.elements.components.CustomTopBar
import com.example.mydailydriver.ui.elements.components.NoteItem

// ✅ Zustandsbehaftet
@Composable
fun GroupScreen(
    viewModel: GroupViewModel = viewModel(factory = AppViewModelProvider),
    groupId: String?,                    // ✅ immer vorhanden!
    onBack: () -> Unit = {},
    onEditNote: (Note) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

//    LaunchedEffect(groupId) {
//        viewModel.selectGroupById(groupId)  // ✅ Gruppe laden
//    }

    GroupContent(
        uiState = uiState,
        onBack = onBack,
        onEditNote = onEditNote,
    )
}

// ✅ Zustandslos
@Composable
fun GroupContent(
    uiState: GroupUiState,
    onBack: () -> Unit = {},
    onEditNote: (Note) -> Unit = {},
) {
    AppScaffold(
        topBar = {
            CustomTopBar(
                titel = uiState.selectedGroup?.name ?: "Gruppe",
                onBack = onBack  // ✅ zurück zu HomeScreen
            )
        }
    ) { innerPadding ->
        if (uiState.notesInGroup.isEmpty()) {
            EmptyState(
                modifier = Modifier.padding(innerPadding),
                text = "Keine Notizen in dieser Gruppe"
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(uiState.notesInGroup) { note ->
                    NoteItem(
                        note = note,
                        onClick = { onEditNote(note) }
                    )
                }
            }
        }
    }
}

// EmptyState:
@Composable
fun EmptyState(modifier: Modifier = Modifier, text: String) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📝", style = MaterialTheme.typography.displayMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text, style = MaterialTheme.typography.titleMedium)
        }
    }
}

// GroupItem:
@Composable
fun GroupItem(group: NoteGroup, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                group.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (group.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    group.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "${group.notes.size} Notizen",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Formular:
@Composable
fun GroupForm(
    name: String,
    description: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Gruppenname") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Beschreibung (optional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) { Text("Abbrechen") }

            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f),
                enabled = name.isNotBlank()
            ) { Text("Speichern") }
        }
    }
}