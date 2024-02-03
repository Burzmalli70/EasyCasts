package dev.joewilliams.easycasts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dev.joewilliams.easycasts.ui.compose.MainBottomNavigationBar
import dev.joewilliams.easycasts.ui.compose.NavigationGraph
import dev.joewilliams.easycasts.ui.theme.EasyCastsTheme
import dev.joewilliams.easycasts.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            EasyCastsTheme {
                val state by viewModel.mainUiFlow.collectAsState()
                Scaffold(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxSize(),
                    bottomBar = {
                        MainBottomNavigationBar(
                            navController = navController
                        ) {

                        }
                    }
                ) {
                    Box(
                        modifier = Modifier.padding(it)
                    ) {
                        NavigationGraph(navController = navController)
                    }
                }
            }
        }
    }
}
