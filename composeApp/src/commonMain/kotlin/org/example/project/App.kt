package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import org.example.project.data.RivalsCharacter
import org.example.project.network.RivalsApiClient

/**
 * App state para centralizar la lógica de UI y mantener un único punto de verdad
 */
class AppState {
    var characters by mutableStateOf<List<RivalsCharacter>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var selectedCharacter by mutableStateOf<RivalsCharacter?>(null)

    suspend fun loadCharacters() {
        if (characters.isNotEmpty()) return

        isLoading = true
        error = null
        try {
            characters = RivalsApiClient.fetchCharacters()
        } catch (e: Exception) {
            error = "Error loading characters: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    fun selectCharacter(character: RivalsCharacter?) {
        selectedCharacter = character
    }
}

@Composable
fun rememberAppState() = remember { AppState() }

@Composable
fun App() {
    val state = rememberAppState()
    val scope = rememberCoroutineScope()

    // Cargar datos automáticamente al inicio
    LaunchedEffect(Unit) {
        state.loadCharacters()
    }

    AppTheme {
        Box(Modifier.fillMaxSize()) {
            // Pantalla principal
            AnimatedVisibility(
                visible = state.selectedCharacter == null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Marvel Rivals Characters",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Eliminamos el botón para mostrar los personajes

                    if (state.isLoading) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(8.dp))
                    }

                    state.error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                    }

                    // Mostramos siempre los personajes, sin depender de showCharacters
                    Column(Modifier.fillMaxSize()) {
                        if (state.characters.isEmpty() && !state.isLoading) {
                            Text("No characters found",
                                modifier = Modifier.padding(16.dp))
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 180.dp),
                                contentPadding = PaddingValues(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                items(state.characters) { character ->
                                    CharacterCard(
                                        character = character,
                                        onClick = { state.selectCharacter(character) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Pantalla de detalle
            AnimatedVisibility(
                visible = state.selectedCharacter != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                state.selectedCharacter?.let { character ->
                    CharacterDetailScreen(
                        character = character,
                        onBack = { state.selectCharacter(null) }
                    )
                }
            }
        }
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(), // o darkColorScheme()
        typography = Typography(),        // puedes usar la tipografía por defecto
        content = content
    )
}

@Composable
private fun CharacterCard(
    character: RivalsCharacter,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(400.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen de fondo que ocupa todo el contenedor
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                // Usamos la función de utilidad para URL
                val imageUrl = getFullImageUrl(character.imageUrl)

                KamelImage(
                    resource = asyncPainterResource(imageUrl),
                    contentDescription = character.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    onLoading = {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    },
                    onFailure = {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("⚠️")
                            Text(
                                "Image not available",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                )
            }
        }
    }
}