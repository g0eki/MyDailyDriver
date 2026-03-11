package com.example.mydailydriver.ui.elements.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.mydailydriver.ui.elements.components.Home
import com.example.mydailydriver.ui.elements.components.Screens

@Composable
fun Navigation(
    // viewModel: MyDailyDriverViewModel = viewModel()
    ) {
    val navController = rememberNavController()
    val nav = NavigationHelper(
                    navController=navController)

    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = Modifier.fillMaxSize()
    ) {
        with(nav) {
            navHostContent() // this = nav - Alternative: nav.run {navHostContent() }
        }
    }

/*    ModalNavigationDrawer(
        // modifier = Modifier,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.primaryContainer,
                drawerShape = RoundedCornerShape(16.dp),
                drawerTonalElevation = 12.dp
            ) {
                nav.ModalDrawerSheetContent()
            }
        },)
    {
        NavHost(
            navController = navController,
            startDestination = Screens.Start.name,
            modifier = Modifier.fillMaxSize()
        ) {
            with(nav) {
                navHostContent() // this = nav - Alternative: nav.run {navHostContent() }
            }
        }
    }*/

}