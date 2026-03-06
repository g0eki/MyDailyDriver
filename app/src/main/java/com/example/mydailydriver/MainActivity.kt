package com.example.mydailydriver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mydailydriver.ui.elements.navigation.Navigation
import com.example.mydailydriver.ui.theme.MyDailyDriverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDailyDriverTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Navigation()
                }
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Navigation()
//                }
            }
        }
    }
}
