package com.example.examen.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.examen.viewModel.formViewModel.FormViewModel
import com.example.examen.viewModel.formViewModel.model.data
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun formView(navController: NavController, formViewModel: FormViewModel){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cuestionario") }
            )
        }
    ) { formContent(it, navController, formViewModel) }
}

// ... (los imports y la función formView se quedan igual) ...

@Composable
fun formContent(it: PaddingValues, navController: NavController, formViewModel: FormViewModel){
    Column(
        modifier = Modifier
            .padding(it)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            var nombre by remember { mutableStateOf("") }
            var apellidos by remember { mutableStateOf("") }
            var carrera by remember { mutableStateOf("") }

            Text("Nombre")
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Apellidos")
            TextField(
                value = apellidos,
                onValueChange = { apellidos= it },
                keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Carrera")
            TextField(
                value = carrera,
                onValueChange = { carrera = it },
                keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(20.dp))

            var _hasSucceeded = remember { mutableStateOf(false) }
            val hasSucceeded = _hasSucceeded
            var _isActive = remember { mutableStateOf(false) }
            val isActive = _isActive

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    _hasSucceeded.value = false
                    if (formViewModel.list.value.isEmpty()) {
                        formViewModel.add(
                            data(
                                nombre = nombre,
                                apellidos = apellidos,
                                carrera = carrera
                            )
                        )
                    } else {
                        formViewModel.update(
                            data(
                                id = 1,
                                nombre = nombre,
                                apellidos = apellidos,
                                carrera = carrera
                            )
                        )
                    }
                    _hasSucceeded.value = true
                }) {
                    Text("Guardar")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = { _isActive.value = !_isActive.value }) {
                    Text("Ver respuestas")
                }
            }

            if (hasSucceeded.value) {
                Text("Datos guardados exitosamente")
            }

            val items = formViewModel.list.collectAsState().value
            if (isActive.value) {
                if (formViewModel.list.collectAsState().value.isEmpty()) {
                    Text("No hay registros previos")
                } else {
                    items.forEach { item ->
                        Text("Nombre: ${item.nombre}")
                        Text("Apellidos: ${item.apellidos}")
                        Text("Carrera: ${item.carrera}")
                    }
                }
            }
        }

        Button( onClick = {navController.navigate("dashboard")} ) {
            Text("volver")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}