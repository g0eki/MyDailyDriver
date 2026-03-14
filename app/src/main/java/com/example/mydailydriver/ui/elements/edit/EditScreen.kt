package com.example.mydailydriver.ui.elements.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.mydailydriver.data.core.RndNoteTexit
import com.example.mydailydriver.ui.AppViewModelProvider




const val maxChars = 30

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Stateful Composables:
fun EditScreen(
    editViewModel: EditViewModel = viewModel(factory = AppViewModelProvider),
    onBack: (() -> Unit)? = null,
    noteId:  String?= null,
    // onEditActions: List<TopBarAction>? = null
) {
    // alles mit var, remember, mutableStateOf() etc. ZustazEnde ,
    val editUiState by editViewModel.uiState.collectAsState()

    val bodyFocusRequester = remember { FocusRequester() }

    val barActions = listOf<TopBarAction>(
        TopBarAction(
            icon = rememberVectorPainter(Icons.Default.Save),
            contentDescription = "Speichern",
            onClick = {
                if(noteId == null) {
                    editViewModel.addNote(newTitel = editUiState.title, newNote = editUiState.content)
                } else {
                    editViewModel.updateNote(id=noteId, newTitel = editUiState.title, newContent = editUiState.content)
                }
                if (onBack != null) { // toDO:  onBack?.invoke()
                    onBack()
                }
            }
        ),
        TopBarAction(
            // imageVector = Icons.Default.file_save,
            icon = rememberVectorPainter(
                if(!editUiState.readOnly) Icons.Default.Lock else Icons.Default.LockOpen
            ),
            contentDescription = if(!editUiState.readOnly) "Lesemodus inaktiv" else "Lesemodus aktiv",
            onClick = {
                editViewModel.toogleReadMode()
            }
        ),
//        TopBarAction(
//            // imageVector = Icons.Default.file_save,
//            icon = rememberVectorPainter(Icons.Default.FileDownload),
//            contentDescription = "FileDownload",
//            onClick = {
//                /* Bearbeiten Logik */
//                // TODO()
//                Log.d("EditScreen", "Download action tapped (not implemented)")
//            }
//        ),

        TopBarAction(
            icon = painterResource(id = R.drawable.baseline_delete_24),
            // icon = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Löschen",
            onClick = {
                noteId?.let {
                    editViewModel.deleteNote(id = it)
                    if (onBack != null) {
                        onBack()
                    }
                }
                // noteId?.let(viewModel::deleteNote)
            }
        ),
    )

    // Aufruf der zustandslosen UI
    EditContent(
        onBack = onBack,
        noteId = noteId,
        title = editUiState.title,
        onTitelChange = { newTitle -> editViewModel.onTitleChange(newTitle) },
        // Kurzschreibweise wäre: onTitelChange = { viewModel.onTitleChange(it) },
        content = editUiState.content,
        // onContentChange = {uiState.content = it},
        onContentChange = { newContent -> editViewModel.onContentChange(newContent) },
        barActions = barActions,
        bodyFocusRequester = bodyFocusRequester,
        readOnly = editUiState.readOnly
    )

}

//Stateless Composables:  zustandslosen UI
@Composable
fun EditContent(
    onBack: (() -> Unit)? = null,
    noteId: String?,
    title: String,
    onTitelChange: (String) -> Unit = {_ ->},  // explizit ignoriert
    content: String,
    onContentChange: (String) -> Unit = {},  // implizit ignoriert – Kurzschreibweise
    barActions: List<TopBarAction>,
    bodyFocusRequester: FocusRequester,
    readOnly: Boolean
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                titel = title,
                // titel = "Edit",
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
                                text = RndNoteTexit().randomNote().take(maxChars),  // "Titel",
                                // text = "Titel",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                            )
                        }
                        innerTextField()
                    },
                    enabled = !readOnly
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
                                    text = RndNoteTexit().randomNote(), // "Notiz beginnen …",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                                )
                            }
                            innerTextField()
                        },
                        enabled = !readOnly
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
            onBack = { },
            title = RndNoteTexit().randomNote().take(maxChars),
            // title = "Titel",
            // onTitelChange = ,
            content = RndNoteTexit().randomNote(),
            // onContentChange = ,
            barActions = barActions,
            bodyFocusRequester = bodyFocusRequester,
            noteId = "",
            onTitelChange = { },
            onContentChange = { },
            readOnly = false
        )
    }
}


