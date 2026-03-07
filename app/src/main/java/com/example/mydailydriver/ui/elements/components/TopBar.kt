package com.example.mydailydriver.ui.elements.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.vector.ImageVector


data class TopBarAction(
    val imageVector: ImageVector,
    val contentDescription: String?, // String? erlaubt null, falls ein Icon rein dekorativ ist
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    // viewModel: MyDailyDriverViewModel, // fur Zustandsbehaftet
    titel: String,
    onBack: (() -> Unit)? = null,
    barActions: List<TopBarAction>? = null  // Liste von Aktionen oder null
) {
    /*
    // fur Zustandsbehaftet
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
     */

    TopBarStateless(
        titel,
        onBack,
        barActions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarStateless(
    titel: String,
    onBack: (() -> Unit)? = null,
    barActions: List<TopBarAction>? = null  // Liste von Aktionen oder null
) {

    // TopAppBar(
    CenterAlignedTopAppBar(
        title = { Text(titel, modifier=Modifier, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = { onBack.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Zurück"
                    )
                }
            }

        },
        actions = {

            barActions?.forEach { actionTmp ->
                IconButton(onClick = { actionTmp.onClick() }) {
                    Icon(
                        imageVector = actionTmp.imageVector,
                        contentDescription = actionTmp.contentDescription
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )

}

// Preview ✅
@Preview(showBackground = true)
@Composable
fun CustomTopBarPreview(){

    val titel: String = "Home"

    val barActions = listOf<TopBarAction>(
        TopBarAction(
            imageVector = Icons.Default.Edit,
            contentDescription = "Bearbeiten",
            onClick = { /* Bearbeiten Logik */ }
        ),
        TopBarAction(
            imageVector = Icons.Default.Save,
            contentDescription = "Speichern",
            onClick = { /* Bearbeiten Logik */ }
        ),
    )

    CustomTopBar(titel=titel, onBack = {}, barActions = barActions)

}