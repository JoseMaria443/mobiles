package com.yae.navalgonose.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun normalButton(
    title: String,
    onClick: (() -> Unit)? = null
){
    if (onClick == null){
        Button(
            onClick = {
                println("AHHHHHHHHHHHHHHHHHHH")
            }
        ) {
            Text(title)
        }
        return
    }
    Button(
        onClick = {}
    ) {
        Text(title)
    }
}

@Composable
fun customOutlinedButton(){
    OutlinedButton(
        onClick = {}
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = ""
        )
    }
}

@Composable
fun customFloatingActionButton(){
    FloatingActionButton(
        onClick = {}
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = ""
        )
    }
}