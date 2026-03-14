package com.example.mydailydriver.ui

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mydailydriver.MyDailyDriverApplication
import com.example.mydailydriver.ui.elements.home.HomeViewModel
import com.example.mydailydriver.ui.elements.edit.EditViewModel // (Dein anderes ViewModel)
import com.example.mydailydriver.ui.elements.components.EditNote
import androidx.navigation.toRoute

// EINE Fabrik für alle ViewModels der App
val AppViewModelProvider = viewModelFactory {

    // Bauanleitung fürs HomeViewModel
    initializer {
        val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyDailyDriverApplication
        HomeViewModel(repository = app.repository) // Holt das globale Repo
    }

    // Bauanleitung fürs Group-Screen

    // Bauanleitung fürs EditViewModel
    initializer {
        val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyDailyDriverApplication
        val savedStateHandle = createSavedStateHandle()  // ✅ NEU
        // val noteId = savedStateHandle.toRoute<EditNote>().noteId  // ✅ NEU
        val noteId = savedStateHandle.get<String>("noteId")  // ✅ direkt per Key
        Log.d("DEBUG", "AppViewModelProvider noteId: $noteId")  // ✅


        EditViewModel(
            repository = app.repository,
            noteId = noteId  // ✅ NEU
        )
    }

    // Hier kannst du später beliebig viele weitere ViewModels hinzufügen...
}