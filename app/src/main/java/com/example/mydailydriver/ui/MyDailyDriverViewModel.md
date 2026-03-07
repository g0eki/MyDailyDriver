# 1

Die absolute Best Practice in der modernen Android-Entwicklung mit Jetpack Compose, Kotlin und UDF (Unidirectional Data Flow) ist: **Ein ViewModel pro Screen** (bzw. pro Feature).

Ein einziges ViewModel für die komplette App wird in der Android-Welt als starkes **Anti-Pattern** betrachtet.

Hier sind die genauen Gründe, warum das so gehandhabt wird, und wie die Architektur im Detail aussieht:

### 1. Warum "Ein ViewModel pro Screen" der Standard ist

* **Speichermanagement (Lifecycle):** Ein ViewModel ist an den Lebenszyklus seines Scopes gebunden (meistens die Activity, das Fragment oder in Compose der `NavBackStackEntry` im Navigation Component). Wenn ein User einen Screen verlässt (z. B. durch "Zurück" navigieren), wird das ViewModel dieses Screens zerstört (`onCleared()` wird aufgerufen) und der Speicher freigegeben. Bei einem App-weiten ViewModel würde der Speicher für *alle* Screens für immer belegt bleiben, auch wenn der User den Screen gar nicht mehr ansieht.
* **Separation of Concerns (Trennung der Zuständigkeiten):** Jeder Screen hat seine eigene UI-Logik und seinen eigenen Zustand (`UiState`). Ein `LoginViewModel` kümmert sich nur um E-Mail, Passwort und Authentifizierung. Ein `HomeViewModel` kümmert sich um das Laden des Feeds. Das hält den Code sauber, testbar und wartbar.
* **UDF (Unidirectional Data Flow):** In Compose fließt der Zustand (State) nach unten zum Screen und Events (User-Aktionen) fließen nach oben zum ViewModel. Das funktioniert am besten, wenn das ViewModel exakt den Zustand für diesen einen Screen abbildet.

### 2. Warum "Ein ViewModel für die ganze App" problematisch ist

* **Das "God Object":** Dein App-weites ViewModel würde extrem schnell zu einer riesigen Datei mit Tausenden von Zeilen Code anwachsen. Es wüsste alles über den Login, das Profil, die Einstellungen, den Warenkorb etc. Das macht den Code unleserlich und extrem schwer zu warten.
* **Zustands-Chaos:** Wenn mehrere Screens denselben State bearbeiten, kommt es schnell zu Konflikten. Wenn du z. B. einen `isLoading`-State im globalen ViewModel hast, welcher Screen lädt gerade? Der Feed oder das Profil?
* **Schlechte Performance:** Jedes Mal, wenn sich *irgendetwas* im globalen ViewModel ändert, riskierst du Recompositions (Neuzeichnen der UI) in Screens, die diese Daten gar nicht benötigen.

---

### Gibt es Ausnahmen? (Shared ViewModels & Globaler State)

Obwohl die Regel "1 Screen = 1 ViewModel" gilt, gibt es zwei wichtige Nuancen, auf die du stoßen wirst:

**A. Shared ViewModels für Sub-Graphen (Nested Navigation)**
Wenn du einen Ablauf aus mehreren, eng verwandten Screens hast (z. B. einen Checkout-Prozess mit den Screens: *Warenkorb -> Adresse -> Zahlung -> Bestätigung*), ist es üblich, **ein** `CheckoutViewModel` zu haben, das an den übergeordneten *Navigation Graph* dieses Flows gebunden ist. So können diese 4 Screens denselben State teilen.

**B. Wie speichert man dann "App-weiten" Zustand?**
Wenn du Daten hast, die wirklich die ganze App betreffen (z. B. "Ist der User eingeloggt?", aktuelles Theme, oder der generelle Netzwerkstatus), dann gehören diese **nicht** in ein ViewModel.
Dafür nutzt man **Repositories** oder **DataStores**. Diese leben als Singletons in der App (oft via Dependency Injection wie Hilt/Dagger bereitgestellt) und exponieren Kotlin `StateFlow` oder `SharedFlow`.

* **Der Flow:** Die Datenquelle (Repository) -> liefert `Flow` an -> Screen-spezifisches ViewModel -> liefert `UiState` an -> Compose Screen.

---

Möchtest du ein kurzes Code-Beispiel sehen, wie man ein ViewModel in Compose am elegantesten an den Screen bindet (z. B. in Kombination mit Hilt oder der Compose Navigation)?


# 2 
Es ist immer eine exzellente Idee, sich bei Architektur-Entscheidungen direkt auf die offizielle Dokumentation von Google zu berufen! Genau dort wird das "Ein ViewModel pro Screen"-Prinzip mehrfach und sehr deutlich als Best Practice (sogar als "Strongly recommended") hervorgehoben.

Hier sind die wichtigsten offiziellen Quellen und die direkten Zitate für dich:

### 1. Die offizielle Architektur-Empfehlung (App Architecture Recommendations)

In der Zusammenfassung der Best Practices für die Android-Architektur wird explizit gesagt, auf welcher Ebene ViewModels eingesetzt werden sollen.

> **Zitat:** *"**Use ViewModels at screen level. Strongly recommended.** Do not use ViewModels in reusable pieces of UI. You should use ViewModels in: Screen-level composables, Activities/Fragments in Views, Destinations or graphs when using Jetpack Navigation."*
> **Quelle:** [Recommendations for Android architecture](https://developer.android.com/topic/architecture/recommendations)

### 2. Definition des ViewModels im UI Layer

In der Dokumentation zum "UI Layer" (der Präsentationsschicht) definiert Google das ViewModel ganz offiziell als State Holder für exakt *einen* Screen.

> **Zitat:** *"**Key Point:** The ViewModel type is the recommended implementation for the management of **screen-level UI state** with access to the data layer."*
> **Quelle:** [UI layer | App architecture](https://developer.android.com/topic/architecture/ui-layer)

### 3. Spezifisch für Jetpack Compose

In den Richtlinien zur Nutzung von ViewModels innerhalb von Jetpack Compose wird noch einmal gewarnt, dass man ViewModels nicht tief in den UI-Baum (oder gar global) durchreichen soll, sondern sie strikt an den "Root" eines Screens (z. B. eine Navigations-Destination) binden muss.

> **Zitat:** *"Note: Due to their lifecycle and scoping, you should access and call ViewModel instances at **screen-level composables**—that is, close to a root composable called from an activity, fragment, or destination of a Navigation graph. You should **never pass down ViewModel instances to other composables**. Instead, pass only the data they need and functions that perform the required logic as parameters."*
> **Quelle:** [Compose - Other considerations](https://developer.android.com/develop/ui/compose/migrate/other-considerations)

---

### Zusammenfassung für dein Team / dein Projekt

Wenn du diese Architektur gegenüber Kollegen oder in einem Code-Review verteidigen musst, kannst du genau diese drei Argumente aus der Doku anführen:

1. Google nennt es offiziell einen **Screen-level state holder**.
2. Es wird als **Strongly recommended** (Dringend empfohlen) eingestuft.
3. In Compose wird das ViewModel an den Scope des Screens (z.B. den `NavBackStackEntry`) gebunden, was bei einem App-weiten ViewModel das automatische Speichermanagement aushebeln würde.

Soll ich dir zeigen, wie du den Code so aufbaust, dass Compose-Komponenten nur noch die nackten Daten (State) statt des ganzen ViewModels übergeben bekommen (sogenanntes *State Hoisting*)?
