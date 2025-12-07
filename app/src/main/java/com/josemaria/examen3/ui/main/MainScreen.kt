package com.josemaria.examen3.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.josemaria.examen3.data.model.Pokemon
import com.josemaria.examen3.util.Resource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val pokemonState by viewModel.pokemonList.collectAsState()
    val favoritesIds by viewModel.favoritesIds.collectAsState()
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            !viewModel.isListEndReached() && lastVisibleItemIndex >= totalItems - 5 && totalItems > 0
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadPokemonPaginated()
        }
    }

    val scope = rememberCoroutineScope()
    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 5
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Pokédex") }) },
        floatingActionButton = {
            if (showScrollToTop) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Volver al inicio"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = pokemonState) {
                is Resource.Success -> {
                    PokemonList(
                        pokemons = state.data ?: emptyList(),
                        listState = listState,
                        navController = navController,
                        favoritesIds = favoritesIds,
                        onToggleFavorite = viewModel::toggleFavorite
                    )
                }
                is Resource.Error -> {
                    Text(text = "Error: ${state.message}", modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Loading -> {
                    val currentList = state.data ?: emptyList()
                    if (currentList.isEmpty()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        PokemonList(
                            pokemons = currentList,
                            listState = listState,
                            navController = navController,
                            favoritesIds = favoritesIds,
                            onToggleFavorite = viewModel::toggleFavorite
                        )
                        if (!viewModel.isListEndReached()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.BottomCenter))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonList(
    pokemons: List<Pokemon>,
    listState: androidx.compose.foundation.lazy.LazyListState,
    navController: NavController,
    favoritesIds: Set<Int>,
    onToggleFavorite: (Int, String, String) -> Unit
) {
    LazyColumn(state = listState, contentPadding = PaddingValues(8.dp)) {
        items(pokemons) { pokemon ->
            val pokemonId = pokemon.url.trimEnd('/').split("/").last().toInt()
            val isFavorite = favoritesIds.contains(pokemonId)
            
            PokemonItem(
                pokemon = pokemon,
                isFavorite = isFavorite,
                onClick = {
                    navController.navigate("details_screen/${pokemon.name}")
                },
                onFavoriteClick = { id, name, imageUrl ->
                    onToggleFavorite(id, name, imageUrl)
                }
            )
        }
    }
}

@Composable
fun PokemonItem(
    pokemon: Pokemon,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: (Int, String, String) -> Unit
) {
    val pokemonId = pokemon.url.trimEnd('/').split("/").last().toInt()
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = pokemon.name,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = pokemon.name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "#${pokemonId.toString().padStart(3, '0')}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            
            IconButton(
                onClick = {
                    onFavoriteClick(pokemonId, pokemon.name, imageUrl)
                }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (isFavorite) Color(0xFFEE1515) else Color.Gray
                )
            }
        }
    }
}