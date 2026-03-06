package com.example.mydailydriver.ui.elements.property
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable



import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
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
                IconButton(onClick = { onSave() }) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Speichern"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            MaterialTheme.colorScheme.background
        )
    )
}