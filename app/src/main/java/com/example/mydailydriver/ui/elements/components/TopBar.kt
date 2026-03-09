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
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.mydailydriver.R


data class TopBarAction(
    val icon: Painter,
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
        title = {
            Text(titel,
                modifier=Modifier,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge, )
                },
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
                        painter = actionTmp.icon,
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

    val barActions = previewBarActions()

    CustomTopBar(titel=titel, onBack = {}, barActions = barActions)

}


@Composable
fun previewBarActions() = listOf<TopBarAction>(
    TopBarAction(
        icon = rememberVectorPainter(Icons.Default.Save),
        contentDescription = "Speichern",
        onClick = {  }
    ),
    TopBarAction(
        // imageVector = Icons.Default.file_save,
        icon = rememberVectorPainter(Icons.Default.FileDownload),
        contentDescription = "FileDownload",
        onClick = {
            /* Bearbeiten Logik */
            TODO()
        }
    ),

    TopBarAction(
        icon = painterResource(id = R.drawable.outline_file_save_24),
        contentDescription = "Speichern",
        onClick = { /* Save Logik */
            TODO() }
    ),
//    TopBarAction(
//        icon = painterResource(id = R.drawable.outline_file_save_24),
//        contentDescription = "Speichern",
//        onClick = { /* Save Logik */
//            TODO() }
//    ),
)