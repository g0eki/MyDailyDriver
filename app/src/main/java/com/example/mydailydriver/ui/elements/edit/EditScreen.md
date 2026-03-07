# 1

Das Problem ist dass `NoteScreen` jetzt Parameter braucht, aber im Preview kannst du kein echtes ViewModel übergeben.

---

**Lösung – Zustand von ViewModel trennen:**

```kotlin
@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    MyDailyDriverTheme {
        NoteScreen(
            viewModel = viewModel(),  // ❌ geht nicht im Preview!
            onNoteSaved = {}
        )
    }
}
```

Das funktioniert nicht. Deshalb macht man es so:

---

**Zustandslos/Zustandsbehaftet trennen** – du hattest das sogar schon als Kommentar:

```kotlin
// Zustandsbehaftet (hält State)
@Composable
fun NoteScreen(viewModel: MyDailyDriverViewModel, onNoteSaved: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    NoteScreenContent(
        title = title,
        body = body,
        onTitleChange = { title = it },
        onBodyChange = { body = it },
        onSave = { viewModel.addNote(title, body); onNoteSaved() },
        onBack = { onNoteSaved() }
    )
}

// Zustandslos (nur UI, previewbar!)
@Composable
fun NoteScreenContent(
    title: String,
    body: String,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    // dein ganzer Scaffold Code hier
}

// Preview ✅
@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    MyDailyDriverTheme {
        NoteScreenContent(
            title = "Test Titel",
            body = "Test Inhalt",
            onTitleChange = {},
            onBodyChange = {},
            onSave = {},
            onBack = {}
        )
    }
}
```

Das ist das **State Hoisting** Pattern – der echte Grund warum du den Kommentar bereits hattest! 🎯

# 2

# EditScreen – Lernnotizen

## State-Behaftet vs. Zustandslos (Stateful vs. Stateless)

In Jetpack Compose trennt man Composables in zwei Typen:

- **`EditScreen`** – besitzt den State, darf ihn ändern
- **`EditContent`** – zeigt nur an, meldet Änderungen nach oben

```kotlin
// EditScreen: State lebt hier
var title by remember { mutableStateOf("") }
var content by remember { mutableStateOf("") }
```

---

## Fehler: `val` kann nicht neu zugewiesen werden

### Problem
Funktionsparameter in Kotlin sind **immer `val`** – unveränderlich.

```kotlin
fun EditContent(
    title: String,   // implizit val
    content: String, // implizit val
) {
    BasicTextField(
        onValueChange = { title = it }  // ❌ Compilerfehler: val kann nicht neu zugewiesen werden
    )
}
```

### Warum `var` in Parametern auch nicht hilft
In Kotlin ist es **syntaktisch verboten**, `val` oder `var` bei Funktionsparametern zu schreiben:

```kotlin
fun EditContent(
    val title: String,  // ❌ Compilerfehler!
    var content: String // ❌ Compilerfehler!
)
```

Selbst wenn es erlaubt wäre: Der Wert wäre nur lokal in einer Recomposition geändert – beim nächsten Neuzeichnen käme der alte Wert wieder vom Aufrufer. Die Änderung wäre sofort **verloren**.

---

## Lösung: Callbacks nach oben durchreichen

```kotlin
fun EditContent(
    title: String,
    onTitelChange: (String) -> Unit = {},   // Callback
    content: String,
    onContentChange: (String) -> Unit = {}, // Callback
)
```

```kotlin
BasicTextField(
    value = title,
    onValueChange = onTitelChange // ✅
)
```

In `EditScreen` wird der State dann tatsächlich geändert:

```kotlin
EditContent(
    title = title,
    onTitelChange = { title = it },   // ✅ title ist var → erlaubt
    content = content,
    onContentChange = { content = it }
)
```

---

## Vergleich: Wo darf `title = it` stehen?

| | `EditScreen` | `EditContent` |
|---|---|---|
| `title` ist... | `var` State | `val` Parameter |
| `title = it` | ✅ erlaubt | ❌ verboten |

---

## Zwei Schreibweisen – dasselbe Ergebnis

```kotlin
onValueChange = { onTitelChange(it) }  // Lambda das die Funktion aufruft
onValueChange = onTitelChange           // Direkte Funktionsreferenz (Kurzform)
```

Beide sind korrekt und identisch in ihrer Wirkung.

---

## Defaultwert `= {}` – warum kein Fehler?

```kotlin
onTitelChange: (String) -> Unit = {}
```

`{}` ist ein Lambda das nichts tut – es **ignoriert den String stillschweigend**. Kotlin erlaubt das, weil der Typ nur verlangt: *„nimm einen String entgegen und gib nichts zurück"*. Das Lambda erfüllt das – es nimmt den String und wirft ihn weg.

Explizit geschrieben wäre es:
```kotlin
= { _ -> } // _ bedeutet: Parameter wird ignoriert
= { }       // Kurzschreibweise dafür
```

---

## `it` – der implizite Lambda-Parameter

```kotlin
onValueChange = { onContentChange(it) }
```

`onValueChange` ist vom Typ `(String) -> Unit`. Das Lambda bekommt automatisch einen Parameter. In Kotlin heißt dieser implizite Parameter immer **`it`**, wenn man ihn nicht selbst benennt.

`it` **ist** der String – er kommt vom `BasicTextField`, wenn der Nutzer tippt.

---

## Zusammenfassung

| Konzept | Erklärung |
|---|---|
| `val` Parameter | Immer unveränderlich, `= it` verboten |
| `var`/`val` in Parametern | Syntaktisch verboten in Kotlin |
| Callback `(String) -> Unit` | Änderung nach oben melden |
| `= {}` Defaultwert | Ignoriert den Parameter stillschweigend |
| `it` | Impliziter Lambda-Parameter |