package com.example.mydailydriver.ui.elements.navigation


import com.example.mydailydriver.R
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.mydailydriver.ui.MyDailyDriverViewModel
import com.example.mydailydriver.ui.elements.edit.EditScreen
import com.example.mydailydriver.ui.elements.components.Screens
import com.example.mydailydriver.ui.elements.home.HomeScreen

// 1. Kein @Composable hier!
internal class NavigationHelper(
    val viewModel: MyDailyDriverViewModel,
    val navController: NavController
) {
    @Composable
    fun ModalDrawerSheetContent() {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.app_name),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Meine Notizen",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )


        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate(Screens.Start.name) }
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Edit, contentDescription = null) },
            label = { Text("Neue Notiz") },
            selected = false,
            onClick = { navController.navigate(Screens.Notes.name) }
        )
    }

    fun NavGraphBuilder.navHostContent() {
        composable(route = Screens.Start.name) {
            HomeScreen(
                viewModel,
                onAddNote = { navController.navigate(route = Screens.Notes.name) }
            )
        }

        composable(route = Screens.Notes.name) {
            val canGoBack = navController.previousBackStackEntry != null

//            val barActions = listOf<TopBarAction>(
//                TopBarAction(
//                    imageVector = Icons.Default.Edit,
//                    contentDescription = "Bearbeiten",
//                    onClick = {
//                        /* Bearbeiten Logik */
//                        TODO()
//                    }
//                ),
//                TopBarAction(
//                    imageVector = Icons.Default.Save,
//                    contentDescription = "Speichern",
//                    onClick = { /* Bearbeiten Logik */ }
//                ),
//            )


            EditScreen(
                viewModel = viewModel,
                onBack = {
                    if (canGoBack) {
                        navController.popBackStack()
                    } else {null}
                },
                // onEditActions = barActions
            )
        }
    }
}