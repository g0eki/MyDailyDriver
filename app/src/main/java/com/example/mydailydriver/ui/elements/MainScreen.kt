package com.example.mydailydriver.ui.elements

import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mydailydriver.ui.theme.MyDailyDriverTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text("Hello My Daily Diver!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyDailyDriverTheme{
        MainScreen()
    }
}