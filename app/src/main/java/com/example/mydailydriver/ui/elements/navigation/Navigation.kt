package com.example.mydailydriver.ui.elements.navigation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.mydailydriver.ui.MyDailyDriverViewModel
import com.example.mydailydriver.ui.elements.components.Screens

@Composable
fun Navigation(viewModel: MyDailyDriverViewModel = viewModel()) {
    val navController = rememberNavController()
    val nav = NavigationHelper(
                    navController=navController)

    ModalNavigationDrawer(
        modifier = Modifier,
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
            modifier = Modifier
        ) {
            with(nav) {
                navHostContent() // this = nav
            }
            /*
            Oder:
            nav.run {navHostContent() } // this = nav
             */
        }
    }

}