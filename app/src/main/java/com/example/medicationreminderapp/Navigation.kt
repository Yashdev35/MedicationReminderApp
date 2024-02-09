package com.example.medicationreminderapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medicationreminderapp.screen.AddEditDetailView
import com.example.medicationreminderapp.screen.HomeScreen

@Composable
fun Navigation(viewModel : MedsViewModel = viewModel(), navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {
        composable(route = Screens.HomeScreen.route) {
            HomeScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = Screens.AddScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                }
            )
            ) {bEntry->
            val id = if(bEntry.arguments != null)bEntry.arguments!!.getLong("id") else 0L
            AddEditDetailView(id = id,viewModel = viewModel, navController = navController)
        }
    }
}
