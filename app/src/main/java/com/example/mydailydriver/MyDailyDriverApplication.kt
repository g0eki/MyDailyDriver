package com.example.mydailydriver

import android.app.Application
import com.example.mydailydriver.data.datastore.NotesStore
import com.example.mydailydriver.data.repository.NoteRepository
import com.example.mydailydriver.data.repository.NoteRepositoryImpl


// TODO() - Erklärung, schreiben

class MyDailyDriverApplication : Application() {

    // Dies ist unser globales Repository für die ganze App!
    lateinit var repository: NoteRepository

    override fun onCreate() {
        super.onCreate()
        // Wird nur EINMAL beim App-Start ausgeführt
        val store = NotesStore(this)
        repository = NoteRepositoryImpl(store)
    }
}