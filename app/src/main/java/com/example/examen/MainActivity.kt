package com.example.examen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.examen.navManager.NavManager
import com.example.examen.viewModel.themeViewModel.ThemeViewModel
import com.example.examen.viewModel.formViewModel.FormViewModel
import com.example.examen.ui.theme.ExamenTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themeViewModel = ThemeViewModel(this)
        setContent {
            val selectedTheme = themeViewModel.getTheme.collectAsState(false)
            val formViewModel: FormViewModel by viewModels()
            ExamenTheme(
                darkTheme = selectedTheme.value
            ) {
                Surface(modifier =
                    Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {  }
                NavManager(themeViewModel, formViewModel)
            }
        }
    }
}