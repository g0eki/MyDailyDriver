/*
package com.example.mydailydriver.data.datastore.di

import android.content.Context
import com.example.mydailydriver.data.datastore.NotesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

*/
/**
 * Hilt DI Module für den NotesStore.
 *
 * @Module      → Markiert diese Klasse als Hilt-Modul (Rezeptbuch für Objekte)
 * @InstallIn   → Bestimmt den Scope: SingletonComponent = lebt so lange wie die App
 *//*

@Module
@InstallIn(SingletonComponent::class)
object `NotesStoreProvider.kt` {

    */
/**
     * Erstellt und liefert eine einzige Instanz des NotesStore.
     *
     * @Provides    → Hilt weiß: "Diese Funktion erstellt ein NotesStore-Objekt"
     * @Singleton   → Nur EINE Instanz für die gesamte App-Laufzeit
     *
     * @ApplicationContext → Hilt injiziert automatisch den App-Context (kein Activity-Context!)
     *//*

    @Provides
    @Singleton
    fun provideNotesStore(
        @ApplicationContext context: Context
    ): NotesStore {
        return NotesStore(context)
    }
}*/
