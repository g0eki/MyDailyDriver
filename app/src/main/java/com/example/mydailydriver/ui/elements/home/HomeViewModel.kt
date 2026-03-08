package com.example.mydailydriver.ui.elements.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mydailydriver.data.datastore.Note
import com.example.mydailydriver.data.datastore.NotesStore
import kotlinx.coroutines.flow.Flow

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val notesStore = NotesStore(application)

    val notes: Flow<List<Note>>
        get() = notesStore.notes
}

/*
https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-6-pathway-2%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-compose-persisting-data-room#10
https://github.com/google-developer-training/basic-android-kotlin-compose-training-inventory-app/tree/room
https://github.com/google-developer-training/basic-android-kotlin-compose-training-inventory-app/tree/room
*/

