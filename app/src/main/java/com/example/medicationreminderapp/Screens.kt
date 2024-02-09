package com.example.medicationreminderapp

sealed class Screens(val route: String) {
    object HomeScreen : Screens("home_screen")
    object AddScreen : Screens("add_screen")
}