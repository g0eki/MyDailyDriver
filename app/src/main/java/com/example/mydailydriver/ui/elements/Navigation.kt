package com.example.mydailydriver.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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

fun Navigation() {
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
                MainScreen()
            }

            composable(route= Screens.Notes.name) {
                NoteScreen()
                // Oder eine Neue Notiz soll ein Screen sein
            }
        }
    }

}