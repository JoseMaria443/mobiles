package com.yae.navalgonose.presentation.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.yae.navalgonose.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun homeView(navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cuarto") }
            )
        },
        floatingActionButton = { customFloatingActionButton() }
    ) {
        Content(it, navController)
    }
}

@Composable
fun content(innerPaddingValues: PaddingValues, navController: NavController){
    Column(
        modifier = Modifier
            .padding(innerPaddingValues)
    ) {
        NormalButton("Click", onClick = {
            navController.navigate("details")
        })
        NormalButton("click 2")
        NormalButton("click 3"){
            println("ayuda mi lap esta a punto de explotaaaar a 100 el ram y el disco ya la ocupo cambiar alch")
        }
        customOutlinedButton()
        customIconButton()
    }
}