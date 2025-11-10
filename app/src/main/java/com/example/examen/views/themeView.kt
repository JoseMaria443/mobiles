package com.example.examen.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.examen.viewModel.themeViewModel.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun themeView(navController: NavController, themeViewModel: ThemeViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cambair tema de la app") }
            )
        }
    ) { paddingValues ->
        themeContent(paddingValues, navController, themeViewModel)
    }
}

@Composable
fun themeContent(
    paddingValues: PaddingValues,
    navController: NavController,
    themeViewModel: ThemeViewModel
) {
    val isDarkTheme by themeViewModel.getTheme.collectAsState(false)

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { newCheckedState ->
                    themeViewModel.saveTheme(newCheckedState)
                },

                modifier = Modifier.scale(2.3f)
            )
        }

        Button(
            onClick = { navController.navigate("dashboard") },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Text("Volver")
        }
    }
}