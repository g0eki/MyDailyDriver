package com.example.mydailydriver.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mydailydriver.MyDailyDriverApplication
import com.example.mydailydriver.ui.elements.home.HomeViewModel
import com.example.mydailydriver.ui.elements.edit.EditViewModel // (Dein anderes ViewModel)

// EINE Fabrik für alle ViewModels der App
val AppViewModelProvider = viewModelFactory {

    // Bauanleitung fürs HomeViewModel
    initializer {
        val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyDailyDriverApplication
        HomeViewModel(repository = app.repository) // Holt das globale Repo
    }

    // Bauanleitung fürs EditViewModel
    initializer {
        val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyDailyDriverApplication
        EditViewModel(repository = app.repository) // Holt dasselbe Repo!
    }

    // Hier kannst du später beliebig viele weitere ViewModels hinzufügen...
}