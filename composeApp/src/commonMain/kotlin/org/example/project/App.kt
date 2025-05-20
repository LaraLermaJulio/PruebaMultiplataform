package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import org.example.project.data.MarvelCharacter
import org.example.project.network.MarvelApiClient

@Composable
fun App() {
    val scope = rememberCoroutineScope()
    var characters by remember { mutableStateOf<List<MarvelCharacter>>(emptyList()) }
    var offset by remember { mutableStateOf(0) }
    var showCharacters by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    MaterialTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                showCharacters = !showCharacters
                if (showCharacters && characters.isEmpty()) {
                    isLoading = true
                    error = null
                    scope.launch {
                        try {
                            characters = MarvelApiClient.fetchCharacters(offset = offset)
                            isLoading = false
                        } catch (e: Exception) {
                            error = "Error al cargar personajes: ${e.message}"
                            isLoading = false
                        }
                    }
                }
            }) {
                Text(if (showCharacters) "Ocultar personajes" else "Mostrar personajes de Marvel")
            }

            Spacer(Modifier.height(8.dp))

            if (isLoading) {
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
            }

            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            AnimatedVisibility(showCharacters) {
                Column(Modifier.fillMaxSize()) {
                    if (characters.isEmpty() && !isLoading) {
                        Text("No se encontraron personajes",
                            modifier = Modifier.padding(16.dp))
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 180.dp),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(characters) { character ->
                                CharacterCard(character)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = {
                                isLoading = true
                                scope.launch {
                                    try {
                                        val newOffset = offset + characters.size
                                        val newCharacters = MarvelApiClient.fetchCharacters(offset = newOffset)
                                        if (newCharacters.isNotEmpty()) {
                                            characters = characters + newCharacters
                                            offset = newOffset
                                        } else {
                                            error = "No hay más personajes disponibles"
                                        }
                                        isLoading = false
                                    } catch (e: Exception) {
                                        error = "Error al cargar más personajes: ${e.message}"
                                        isLoading = false
                                    }
                                }
                            },
                            enabled = !isLoading && characters.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (isLoading) "Cargando..." else "Cargar más")
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterCard(character: MarvelCharacter) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(280.dp)
    ) {
        Column {
            // Imagen del personaje
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                KamelImage(
                    resource = asyncPainterResource(character.thumbnail.getUrl()),
                    contentDescription = character.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onLoading = {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    },
                    onFailure = {
                        Text("⚠️", Modifier.align(Alignment.Center))
                    }
                )
            }
            // Datos del personaje
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                if (character.description.isNotEmpty()) {
                    Text(
                        text = character.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = "Sin descripción disponible",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}