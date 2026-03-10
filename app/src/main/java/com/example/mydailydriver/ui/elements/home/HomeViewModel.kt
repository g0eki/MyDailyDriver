package com.example.mydailydriver.ui.elements.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mydailydriver.data.models.Note
import com.example.mydailydriver.data.datastore.NotesStore
import com.example.mydailydriver.data.repository.NoteRepository
import com.example.mydailydriver.data.repository.NoteRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * DAS VIEWMODEL
 * * Beachte: Wir erben jetzt von 'ViewModel()' und NICHT mehr von 'AndroidViewModel()'.
 * Warum? Weil dieses ViewModel keinen 'Context' mehr braucht.
 * Es bekommt seine Datenquelle (das Repository) bereits komplett fertig von außen "geschenkt".
 * * WICHTIG: Wir fordern im Konstruktor das INTERFACE (NoteRepository) an,
 * nicht die konkrete Klasse (NoteRepositoryImpl). Das ViewModel weiß also nicht,
 * ob die Daten aus dem DataStore, aus Room oder aus dem Internet kommen.
 */
class HomeViewModel(
    private val repository: NoteRepository
): ViewModel() {

    // Wir holen uns den Flow (die reaktive Liste von Notizen) direkt aus dem Repository.
    // Sobald sich in der Datenbank etwas ändert, wird dieser Flow automatisch aktualisiert
    // und die UI zeichnet sich neu.
    val notes: Flow<List<Note>> = repository.allNotes

    // Funktion zum Hinzufügen einer Notiz.
    // Wir nutzen den viewModelScope, weil Datenbank-Operationen asynchron (suspend)
    // ablaufen müssen, damit die App nicht einfriert.
    fun addNote(newTitel: String, newNote: String) {
        viewModelScope.launch {
            // Wir sagen nur dem Repository: "Speicher das!". Wie das passiert, ist dem ViewModel egal.
            repository.addNote(title = newTitel, content = newNote)
        }
    }
}

/**
 * DIE FACTORY (Die Fabrik)
 * * Problem: Android weiß standardmäßig nur, wie man leere ViewModels (ohne Parameter) erstellt.
 * Unser HomeViewModel braucht aber ein 'NoteRepository' im Konstruktor.
 * Lösung: Wir schreiben eine "Bauanleitung" (Factory), die Android erklärt,
 * wie das ViewModel inklusive seiner Abhängigkeiten zusammengebaut wird.
 */
val HomeViewModelFactory = viewModelFactory {
    // Der 'initializer' Block beschreibt, was passiert, wenn die UI das ViewModel anfordert.
    initializer {

        // 1. Context besorgen:
        // Obwohl das ViewModel keinen Context mehr hat, braucht der NotesStore ihn noch (um Dateien zu speichern).
        // Über 'APPLICATION_KEY' können wir uns den Context der gesamten App sicher holen.
        val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!

        // 2. Datenbank/Store erstellen:
        // Wir erstellen unsere DataStore-Klasse und geben ihr den Application-Context.
        val store = NotesStore(application)

        // 3. Repository erstellen:
        // Wir stecken den fertigen Store in unsere konkrete Repository-Implementierung.
        // HIER ist der Ort, an dem später (für Room) Änderungen vorgenommen werden!
        val repository = NoteRepositoryImpl(store)

        // 4. ViewModel erstellen und zurückgeben:
        // Wir stecken das fertige Repository in das ViewModel.
        // Ab jetzt kann das ViewModel arbeiten!
        HomeViewModel(repository)
    }
}