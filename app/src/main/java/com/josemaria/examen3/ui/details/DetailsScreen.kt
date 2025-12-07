package com.josemaria.examen3.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.josemaria.examen3.data.model.PokemonDetail
import com.josemaria.examen3.util.Resource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    pokemonName: String,
    viewModel: DetailsViewModel
) {
    val pokemonDetailState by viewModel.pokemonDetail.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(pokemonName) {
        viewModel.loadPokemonDetail(pokemonName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pokemonName.replaceFirstChar { it.uppercase() }) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    when (val state = pokemonDetailState) {
                        is Resource.Success -> {
                            IconButton(onClick = { viewModel.toggleFavorite(state.data!!) }) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = if (isFavorite) "Quitar de Favoritos" else "Añadir a Favoritos",
                                    tint = if (isFavorite) Color(0xFFEE1515) else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        else -> {}
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val state = pokemonDetailState) {
                is Resource.Success -> {
                    DetailContent(
                        detail = state.data!!
                    )
                }
                is Resource.Error -> {
                    Text("Error cargando detalles: ${state.message}")
                }
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun DetailContent(
    detail: PokemonDetail
) {
    var isShiny by remember { mutableStateOf(false) }
    
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            AsyncImage(
                model = if (isShiny) detail.sprites.frontShiny else detail.sprites.frontDefault,
                contentDescription = "${detail.name} ${if (isShiny) "shiny" else "normal"}",
                modifier = Modifier.size(200.dp)
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { isShiny = !isShiny },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isShiny) Color(0xFFFFD700) else Color.Transparent
                )
            ) {
                Text(
                    text = if (isShiny) "Versión Shiny" else "Versión Normal",
                    color = if (isShiny) Color.Black else MaterialTheme.colorScheme.primary
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "#${detail.id.toString().padStart(3, '0')}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = detail.name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Altura", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Text("${detail.height * 10} cm", style = MaterialTheme.typography.bodyLarge)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Peso", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Text("${detail.weight / 10.0} kg", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Tipos",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        detail.types.forEach { typeSlot ->
                            Surface(
                                modifier = Modifier.padding(end = 8.dp),
                                shape = RoundedCornerShape(8.dp),
                                color = getTypeColor(typeSlot.type.name)
                            ) {
                                Text(
                                    text = typeSlot.type.name.uppercase(),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Habilidades",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    detail.abilities.take(3).forEach { abilitySlot ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "•",
                                modifier = Modifier.padding(end = 8.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = abilitySlot.ability.name.replace("-", " ").replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (abilitySlot.isHidden) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.secondary
                                ) {
                                    Text(
                                        text = "Oculta",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Movimientos Principales",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Primeros 10 movimientos disponibles",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    detail.moves.take(10).forEach { move ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "•",
                                modifier = Modifier.padding(end = 8.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = move.move.name.replace("-", " ").replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
        

    }
}

@Composable
fun getTypeColor(typeName: String): Color {
    return when (typeName.lowercase()) {
        "normal" -> Color(0xFFA8A878)
        "fire" -> Color(0xFFF08030)
        "water" -> Color(0xFF6890F0)
        "electric" -> Color(0xFFF8D030)
        "grass" -> Color(0xFF78C850)
        "ice" -> Color(0xFF98D8D8)
        "fighting" -> Color(0xFFC03028)
        "poison" -> Color(0xFFA040A0)
        "ground" -> Color(0xFFE0C068)
        "flying" -> Color(0xFFA890F0)
        "psychic" -> Color(0xFFF85888)
        "bug" -> Color(0xFFA8B820)
        "rock" -> Color(0xFFB8A038)
        "ghost" -> Color(0xFF705898)
        "dragon" -> Color(0xFF7038F8)
        "dark" -> Color(0xFF705848)
        "steel" -> Color(0xFFB8B8D0)
        "fairy" -> Color(0xFFEE99AC)
        else -> Color.Gray
    }
}