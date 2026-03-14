package com.example.mydailydriver.ui.elements.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mydailydriver.R
import com.example.mydailydriver.data.models.NoteGroup

@Composable
fun AppDrawerContent(
    onNavigateHome: () -> Unit = {},
    onAddNotesGroups: () -> Unit,
    noteGroups: List<NoteGroup> = emptyList(),
    onSelectGroup: (NoteGroup) -> Unit = {},
){
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
            text = "Meine Gruppen",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Edit, contentDescription = null) },
            label = { Text("Neue Gruppe") },
            selected = false,
            onClick = { onAddNotesGroups() }
        )

        if(!noteGroups.isEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.height(16.dp))
            noteGroups.forEach { group ->
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Apps, contentDescription = null) },
                    label = { Text(group.name) },
                    selected = false,
                    onClick = { onSelectGroup(group)  } // TODO: Die Gruppe öfnnen
                )
            }
        }

    }
}