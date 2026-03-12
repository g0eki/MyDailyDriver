package com.example.mydailydriver.ui.elements.components

import androidx.annotation.StringRes
import com.example.mydailydriver.R
import com.example.mydailydriver.data.models.Note

import kotlinx.serialization.Serializable

// enum class Screens(@StringRes val title: Int) {
enum class Screens(@get:StringRes val title: Int) {
    Start(title = R.string.app_name),
    Notes(title = R.string.edit_notes),
}

@Serializable
object Home

@Serializable
data class EditNote(val noteId: String? = null)