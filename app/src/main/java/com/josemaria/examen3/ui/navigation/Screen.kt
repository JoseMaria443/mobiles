package com.josemaria.examen3.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main_screen")
    object Search : Screen("search_screen")
    object Favorites : Screen("favorites_screen")
    object Details : Screen("details_screen/{pokemonName}") {
        fun createRoute(pokemonName: String) = "details_screen/$pokemonName"
    }
}