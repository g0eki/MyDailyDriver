package com.example.mydailydriver.ui.elements.edit

import android.R.attr.contentDescription
import android.R.attr.onClick
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mydailydriver.ui.elements.components.CustomTopBar
import com.example.mydailydriver.ui.elements.components.TopBarAction
import com.example.mydailydriver.ui.elements.components.previewBarActions
import com.example.mydailydriver.ui.theme.MyDailyDriverTheme
import com.example.mydailydriver.R
import com.example.mydailydriver.data.core.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Stateful Composables:
fun EditScreen(
    viewModel: EditViewModel = viewModel(),
    onBack: (() -> Unit)? = null,
    // onEditActions: List<TopBarAction>? = null
) {
    // alles mit var, remember, mutableStateOf() etc. ZustazEnde ,
    var title by rememberSaveable() { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }

    val bodyFocusRequester = remember { FocusRequester() }

    val barActions = listOf<TopBarAction>(
        TopBarAction(
            icon = rememberVectorPainter(Icons.Default.Save),
            contentDescription = "Speichern",
            onClick = {
                viewModel.addNote(newTitel = title, newNote = content)
                if (onBack != null) {
                    onBack()
                }
            }
        ),
        TopBarAction(
            // imageVector = Icons.Default.file_save,
            icon = rememberVectorPainter(Icons.Default.FileDownload),
            contentDescription = "FileDownload",
            onClick = {
                /* Bearbeiten Logik */
                // TODO()
                Log.d("EditScreen", "Download action tapped (not implemented)")
            }
        ),

        TopBarAction(
            icon = painterResource(id = R.drawable.outline_file_save_24),
            contentDescription = "Speichern",
            onClick = { /* Save Logik */
                // TODO()
                Log.d("EditScreen", "Download action tapped (not implemented)")
            }
        ),
    )

    // Aufruf der zustandslosen UI
    EditContent(
        onBack = onBack,
        title = title,
        onTitelChange = { title = it },
        content = content,
        onContentChange = {content = it},
        barActions = barActions,
        bodyFocusRequester = bodyFocusRequester
    )

}

//Stateless Composables:  zustandslosen UI
@Composable
fun EditContent(
    onBack: (() -> Unit)? = null,
    title: String,
    onTitelChange: (String) -> Unit = {_ ->},  // explizit ignoriert
    content: String,
    onContentChange: (String) -> Unit = {},  // implizit ignoriert – Kurzschreibweise
    barActions: List<TopBarAction>,
    bodyFocusRequester: FocusRequester
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                titel = title,
                onBack = onBack,
                barActions = barActions
            )
        }
    ) { innerPadding ->
        // Trailing Lambda Syntax
        Surface{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // Also ich kann noch Surface(....) { BasicTextField(...) ... } Aufrufen
                // Titel
                // toDO: private fun CustomTextField(
                BasicTextField(
                    value = title,
                    onValueChange = onTitelChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorationBox = { innerTextField ->
                        if (title.isEmpty()) {
                            Text(
                                text = Note().randomNote(),  // "Titel",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                            )
                        }
                        innerTextField()
                    }
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Notiztext
                Surface() {
                    BasicTextField(
                        value = content,
                        onValueChange = { onContentChange(it) },
                        modifier = Modifier
                            .fillMaxSize()
                            .focusRequester(bodyFocusRequester),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            if (content.isEmpty()) {
                                Text(
                                    text = Note().randomNote(), // "Notiz beginnen …",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                                )
                            }
                            innerTextField()
                        }
                    )
                }

        }

        }
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit = {},
    placeholder: String,
    modifier: Modifier,
    textStyle: TextStyle,
    cursorBrush: Brush,
    )
{
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle,
        cursorBrush = cursorBrush,
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = textStyle.copy(
                        color = textStyle.color.copy(alpha = 0.4f)
                    ),)
            }
            innerTextField()
        }
    )
}


@Preview(showBackground = true)
@Composable
fun EditScreenPreview() {

    val bodyFocusRequester = remember { FocusRequester() }
    val barActions = previewBarActions()

    MyDailyDriverTheme{
        EditContent(
            onBack = {  },
            title = Note().randomNote(),
            // onTitelChange = ,
            content = Note().randomNote(),
            // onContentChange = ,
            barActions = barActions,
            bodyFocusRequester = bodyFocusRequester
            )
    }
}


