package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.AIConciergeScreen
import com.example.ui.GuestDashboard
import com.example.ui.LandingScreen
import com.example.ui.StaffDashboard
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.HMSViewModel

const val ROUTE_LANDING = "landing"
const val ROUTE_GUEST = "guest"
const val ROUTE_STAFF = "staff"
const val ROUTE_CHAT = "chat"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable Edge-to-Edge full screen content rendering
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Root NavHost navigation setup
                    HotelAppNavigationContainer()
                }
            }
        }
    }
}

@Composable
fun HotelAppNavigationContainer() {
    val navController = rememberNavController()
    val viewModel: HMSViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = ROUTE_LANDING
    ) {
        composable(ROUTE_LANDING) {
            LandingScreen(
                viewModel = viewModel,
                onNavigateToGuest = { navController.navigate(ROUTE_GUEST) },
                onNavigateToStaff = { navController.navigate(ROUTE_STAFF) }
            )
        }

        composable(ROUTE_GUEST) {
            GuestDashboard(
                viewModel = viewModel,
                onNavigateToChat = { navController.navigate(ROUTE_CHAT) },
                onNavigateBack = {
                    viewModel.logout()
                    navController.popBackStack(ROUTE_LANDING, false)
                }
            )
        }

        composable(ROUTE_STAFF) {
            StaffDashboard(
                viewModel = viewModel,
                onNavigateBack = {
                    viewModel.logout()
                    navController.popBackStack(ROUTE_LANDING, false)
                }
            )
        }

        composable(ROUTE_CHAT) {
            AIConciergeScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
