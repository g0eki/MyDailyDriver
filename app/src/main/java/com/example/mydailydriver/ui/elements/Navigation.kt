package com.example.mydailydriver.ui.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mydailydriver.ui.MyDailyDriverViewModel
import com.example.mydailydriver.ui.elements.home.HomeScreen

@Composable
// fun Navigation(viewModel: MovieMakerViewModel = viewModel()) {
//fun Navigation() {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController,
//        startDestination = Screens.Start.name,
//        modifier = Modifier) {
//        composable(route= Screens.Start.name) {
//            MainScreen()
//        }
//
//        composable(route= Screens.Notes.name) {
//            NoteScreen()
//        }
//    }
//}

fun Navigation(viewModel: MyDailyDriverViewModel = viewModel()) {
    val navController = rememberNavController()

    ModalNavigationDrawer(
        modifier = Modifier.padding(8.dp),
        drawerContent = {
            ModalDrawerSheet {
// NavigationDrawerItem ==> Über eine Funktion (For / Loop) Ordner oder Notien darstellen
            }
        },)
    {
        NavHost(navController = navController,
            startDestination = Screens.Start.name,
            modifier = Modifier) {
            composable(route= Screens.Start.name) {
                HomeScreen(
                    viewModel,
                    onAddNote = {navController.navigate(route= Screens.Notes.name)}
                )
            }

            composable(route= Screens.Notes.name) {
                NoteScreen(viewModel)
                // Oder eine Neue Notiz soll ein Screen sein
            }
        }
    }

}