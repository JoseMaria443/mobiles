package com.example.examen.navManager

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examen.viewModel.formViewModel.FormViewModel
import com.example.examen.viewModel.themeViewModel.ThemeViewModel
import com.example.examen.views.dashboardView
import com.example.examen.views.formView
import com.example.examen.views.themeView

@Composable
fun NavManager(themeViewModel: ThemeViewModel, formViewModel: FormViewModel){
    val NavController = rememberNavController()

    NavHost(
        navController = NavController,
        startDestination = "dashboard"
    ){
        composable("dashboard") {
            dashboardView(navController = NavController)
        }
        composable("themes") {
            themeView(navController = NavController, themeViewModel)
        }
        composable("form") {
            formView(navController = NavController, formViewModel)
        }
    }
}