package com.josemaria.examen3.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.josemaria.examen3.ui.details.DetailsScreen
import com.josemaria.examen3.ui.details.DetailsViewModel
import com.josemaria.examen3.ui.favorites.FavoritesScreen
import com.josemaria.examen3.ui.favorites.FavoritesViewModel
import com.josemaria.examen3.ui.main.MainScreen
import com.josemaria.examen3.ui.main.MainViewModel
import com.josemaria.examen3.ui.search.SearchScreen
import com.josemaria.examen3.ui.search.SearchViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem("Lista", Screen.Main.route, Icons.Filled.Home),
        BottomNavItem("Buscar", Screen.Search.route, Icons.Filled.Search),
        BottomNavItem("Favoritos", Screen.Favorites.route, Icons.Filled.Favorite)
    )

    val showBottomBar = currentDestination?.route in listOf(
        Screen.Main.route,
        Screen.Search.route,
        Screen.Favorites.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(Screen.Main.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Main.route,
            modifier = Modifier.padding(paddingValues)
        ) {
        composable(route = Screen.Main.route) {
            val viewModel: MainViewModel = hiltViewModel()
            MainScreen(navController = navController, viewModel = viewModel)
        }

        composable(route = Screen.Search.route) {
            val viewModel: SearchViewModel = hiltViewModel()
            SearchScreen(navController = navController, viewModel = viewModel)
        }

        composable(route = Screen.Favorites.route) {
            val viewModel: FavoritesViewModel = hiltViewModel()
            FavoritesScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("pokemonName") { type = NavType.StringType })
        ) { backStackEntry ->
            val pokemonName = backStackEntry.arguments?.getString("pokemonName")

            if (pokemonName != null) {
                val viewModel: DetailsViewModel = hiltViewModel()
                DetailsScreen(navController = navController, pokemonName = pokemonName, viewModel = viewModel)
            }
        }
    }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)