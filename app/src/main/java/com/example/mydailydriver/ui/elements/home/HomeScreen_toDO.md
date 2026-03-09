# 1

Du musst den `ModalNavigationDrawer` aus `Navigation()` raus und nur in `HomeScreen` rein:

---

## Navigation.kt – Drawer raus

```kotlin
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val nav = NavigationHelper(navController = navController)

    // Kein ModalNavigationDrawer mehr hier!
    NavHost(
        navController = navController,
        startDestination = Screens.Start.name,
    ) {
        with(nav) {
            navHostContent()
        }
    }
}
```

---

## HomeScreen.kt – Drawer rein

```kotlin
@Composable
fun HomeScreen(
    onAddNote: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(...) {
                // Drawer Inhalt hier
            }
        }
    ) {
        // Dein eigentlicher HomeScreen Inhalt
        Scaffold(...) { ... }
    }
}
```

---

## Problem dabei

Der Drawer-Inhalt (`NavigationDrawerItem` mit `navController.navigate(...)`) braucht den `navController` – der aber in `NavigationHelper` lebt.

Du hast zwei Optionen:

**Option 1** – `navController` an `HomeScreen` übergeben:
```kotlin
composable(Screens.Start.name) {
    HomeScreen(
        navController = navController,
        onAddNote = { ... }
    )
}
```

**Option 2** – Navigation per Callbacks lösen (sauberer):
```kotlin
HomeScreen(
    onAddNote = { navController.navigate(Screens.Notes.name) },
    onNavigateHome = { navController.navigate(Screens.Start.name) }
)
```

**Option 2 ist Best Practice** – `HomeScreen` kennt dann keinen `navController` direkt.


# 2

Das geht, aber die **beiden Drawer überlagern sich** – Compose würde zwei verschachtelte Drawer haben:

```
Navigation()
└── ModalNavigationDrawer  ← äußerer Drawer
    └── NavHost
        └── HomeScreen
            └── ModalNavigationDrawer  ← innerer Drawer
                └── Inhalt
```

---

## Was passiert konkret?

- Auf `HomeScreen` → **innerer Drawer** reagiert auf Wischgeste
- Auf anderen Screens → **äußerer Drawer** reagiert
- Beide gleichzeitig wischen → **undefiniertes Verhalten**, einer gewinnt

Das klingt nach was du willst – aber in der Praxis ist es **verwirrend für den User**: gleiche Geste, unterschiedlicher Drawer je nach Screen.

---

## Empfehlung

Wenn beide Drawer **denselben Inhalt** haben sollen:
→ Nur in `HomeScreen`, andere Screens kriegen keinen Drawer. ✅

Wenn sie **unterschiedlichen Inhalt** haben sollen:
→ Überlege ob das wirklich nötig ist – zwei verschiedene Drawer auf verschiedenen Screens verwirrt den User.

---

**Was willst du auf den anderen Screens zeigen das sich vom HomeScreen-Drawer unterscheidet?** Dann kann ich dir besser sagen was Sinn macht.