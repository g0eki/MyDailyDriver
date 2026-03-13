package com.example.mydailydriver.ui.elements.edit

data class EditUiState(
    val title: String = "",
    val content: String = "",
    val readOnly: Boolean = false // ODer direkt veränderbar ?
)
/*
toDO():
--------------------------------------------------------------------------------------------------

Dann lass uns **UiState** einführen:

## 1. `EditViewModel` — UiState hinzufügen:

```kotlin
data class EditUiState(
    val title: String = "",
    val content: String = "",
    val isLoading: Boolean = true
)

class EditViewModel(
    private val repository: NoteRepository,
    private val noteId: String? = null,
) : ViewModel() {

    var uiState by mutableStateOf(EditUiState())
        private set  // nur ViewModel kann schreiben!

    init {
        if (noteId != null) {
            viewModelScope.launch {
                val note = repository.getNoteById(noteId)
                if (note != null) {
                    uiState = EditUiState(
                        title = note.title,
                        content = note.content,
                        isLoading = false
                    )
                }
            }
        } else {
            uiState = EditUiState(isLoading = false)  // neue Notiz
        }
    }

    fun onTitleChange(newTitle: String) {
        uiState = uiState.copy(title = newTitle)
    }

    fun onContentChange(newContent: String) {
        uiState = uiState.copy(content = newContent)
    }
}
```

---

## 2. `EditScreen` — UiState verwenden:

```kotlin
fun EditScreen(
    viewModel: EditViewModel = viewModel(factory = AppViewModelProvider),
    onBack: (() -> Unit)? = null,
    noteId: String? = null,
) {
    val uiState = viewModel.uiState

    EditContent(
        title = uiState.title,
        onTitelChange = { viewModel.onTitleChange(it) },
        content = uiState.content,
        onContentChange = { viewModel.onContentChange(it) },
        ...
    )
}
```

Soll ich das komplett für dich zusammenbauen?







 */

