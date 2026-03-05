# NotesStoreProvider.kt – Erklärung & Hintergrund
## !!! Nicht umgesetzt !!!
---

## Was macht diese Datei?

`NotesStoreProvider.kt` ist ein **Hilt-Modul** – eine Art Rezeptbuch für Dependency Injection.
Es sagt Hilt: *„Wenn irgendwo ein `NotesStore` gebraucht wird – hier ist das Rezept, wie man ihn erstellt."*

---

## Der Code Schritt für Schritt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NotesStoreProvider {
    ...
}
```

| Annotation / Keyword | Bedeutung |
|---|---|
| `@Module` | Markiert die Klasse als Hilt-Modul (Rezeptbuch) |
| `@InstallIn(SingletonComponent::class)` | Der NotesStore lebt so lange wie die **gesamte App** |
| `object` | Kotlin-Singleton – kein `class`, da keine Instanz nötig |

---

```kotlin
@Provides
@Singleton
fun provideNotesStore(
    @ApplicationContext context: Context
): NotesStore {
    return NotesStore(context)
}
```

| Annotation | Bedeutung |
|---|---|
| `@Provides` | Diese Funktion **erstellt** das Objekt für Hilt |
| `@Singleton` | Nur **eine** Instanz für die gesamte App-Laufzeit |
| `@ApplicationContext` | Hilt liefert automatisch den App-Context (sicherer als Activity-Context!) |

---

## Warum `@ApplicationContext` und nicht einfach `Context`?

In Android gibt es mehrere Context-Typen:

| Context-Typ | Lebt so lange wie... | Gefahr |
|---|---|---|
| `ActivityContext` | Die Activity | Memory Leak wenn Activity zerstört wird! |
| `ApplicationContext` | Die gesamte App | ✅ Sicher für Singletons |

Da unser `NotesStore` ein Singleton ist (lebt die gesamte App-Zeit), **muss** er den `ApplicationContext` nutzen – sonst riskieren wir Memory Leaks.

---

## Das Problem das DI löst

### Ohne DI – überall selbst erstellen:
```kotlin
class NotesScreen {
    val store = NotesStore(context)  // Eigene Instanz
}

class SearchScreen {
    val store = NotesStore(context)  // Wieder eigene Instanz
}

class SettingsScreen {
    val store = NotesStore(context)  // Noch eine Instanz
}
```
❌ Drei verschiedene Instanzen → mögliches Datenchaos
❌ Code-Wiederholung
❌ Schwer zu testen

### Mit DI (Hilt) – einmal definieren, überall injizieren:
```kotlin
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val store: NotesStore  // Hilt liefert die EINE Instanz
) : ViewModel()

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val store: NotesStore  // Dieselbe Instanz!
) : ViewModel()
```
✅ Immer dieselbe Instanz
✅ Kein doppelter Code
✅ Einfach testbar (Mock-Objekte möglich)

---

## Wie funktioniert das Zusammenspiel?

```
App startet
    │
    ▼
Hilt liest @Module
    │
    ▼
NotesStoreProvider.provideNotesStore() wird aufgerufen
    │
    ▼
Eine NotesStore-Instanz wird erstellt & gespeichert (@Singleton)
    │
    ▼
Jeder ViewModel der NotesStore braucht → bekommt dieselbe Instanz
```

---

## Benötigte Dependencies (`build.gradle`)

```kotlin
// Hilt
implementation("com.google.dagger:hilt-android:2.48")
kapt("com.google.dagger:hilt-compiler:2.48")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.0.0")

// JSON Serialization
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
```

Und im `plugins`-Block:
```kotlin
plugins {
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
}
```

Und die App-Klasse muss annotiert werden:
```kotlin
@HiltAndroidApp
class MyApp : Application()
```

---

## Vollständige Dateistruktur

```
📁 data/
│   ├── 📄 Note.kt              → data class Note (Datenmodell)
│   └── 📄 NotesStore.kt        → DataStore Logik
│
📁 di/
│   └── 📄 NotesStoreProvider.kt → DI Modul (diese Datei)
│
📁 ui/
│   └── 📄 NotesViewModel.kt    → ViewModel das NotesStore injiziert bekommt
│
📄 MyApp.kt                     → @HiltAndroidApp Application Klasse
```

---

## Quellen zum Weiterlesen

### Hilt / Dependency Injection
- **Offizielle Android Docs – Hilt:**
  https://developer.android.com/training/dependency-injection/hilt-android

- **Offizielle Android Docs – DI Grundlagen:**
  https://developer.android.com/training/dependency-injection

- **Codelab – Using Hilt (Google):**
  https://developer.android.com/codelabs/android-hilt

### DataStore
- **Offizielle Android Docs – DataStore:**
  https://developer.android.com/topic/libraries/architecture/datastore

- **DataStore mit Hilt kombinieren:**
  https://developer.android.com/codelabs/android-preferences-datastore

### Kotlin
- **Kotlin – Object Declarations (Singleton):**
  https://kotlinlang.org/docs/object-declarations.html

- **Kotlin – Annotations:**
  https://kotlinlang.org/docs/annotations.html

### Videos (Deutsch/Englisch)
- **Philipp Lackner – Hilt Dependency Injection (YouTube):**
  https://www.youtube.com/watch?v=bbMsuI2p1DQ

- **Philipp Lackner – DataStore Tutorial (YouTube):**
  https://www.youtube.com/watch?v=4bJo-SB2yiU

---

> 💡 **Tipp für den Einstieg:** Lies zuerst das offizielle Hilt-Codelab von Google durch –
> es erklärt DI Schritt für Schritt anhand eines echten Projekts.