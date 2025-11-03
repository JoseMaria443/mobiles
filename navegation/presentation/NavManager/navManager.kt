package com.yae.navalgonose.presentation.NavManager

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavManager(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "Home"
    ){
        composable("Home"){
            HomeView(navController)
        }
        composable("Details"){
            DetailsView(navController)
        }
    }
}