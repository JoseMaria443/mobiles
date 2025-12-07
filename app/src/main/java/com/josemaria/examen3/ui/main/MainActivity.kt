package com.josemaria.examen3.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.josemaria.examen3.ui.navigation.NavGraph
import com.josemaria.examen3.ui.theme.Examen3Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Examen3Theme {
                Surface {
                    NavGraph()
                }
            }
        }
    }
}
