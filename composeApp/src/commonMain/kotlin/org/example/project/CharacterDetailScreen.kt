package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.example.project.data.RivalsCharacter

@Composable
fun CharacterDetailScreen(
    character: RivalsCharacter,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Cabecera con imagen y datos básicos
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
                    }

                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(48.dp)) // Para equilibrar la cabecera
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Imagen y datos básicos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Imagen mejorada con tamaño ajustado y mejor manejo de errores
                    val imageUrl = getFullImageUrl(character.imageUrl)

                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        KamelImage(
                            resource = asyncPainterResource(imageUrl),
                            contentDescription = character.name,
                            contentScale = ContentScale.Fit, // Cambiado a Fit para mostrar completa
                            modifier = Modifier.fillMaxSize(),
                            onLoading = {
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            },
                            onFailure = {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text("⚠️")
                                    Text(
                                        "Image not available",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        )
                    }

                    Column(Modifier.weight(1f)) {
                        character.realName?.let {
                            Text(
                                text = "Real name: $it",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        character.role?.let {
                            Text(
                                text = "Role: $it",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        character.attackType?.let {
                            Text(
                                text = "Attack type: $it",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        character.team?.let {
                            if (it.isNotEmpty()) {
                                Text(
                                    text = "Team: ${it.joinToString(", ")}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }

                        character.difficulty?.let {
                            Text(
                                text = "Difficulty: $it/5",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Bio y Lore
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(Modifier.padding(16.dp)) {
                    character.bio?.let {
                        Text(
                            text = "Biography",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    character.lore?.let {
                        Text(
                            text = "Lore",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Transformaciones
        character.transformations?.let { transformations ->
            if (transformations.isNotEmpty()) {
                item {
                    Text(
                        text = "Transformations",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(transformations) { transformation ->
                            Card(
                                modifier = Modifier.width(160.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    transformation.icon?.let { iconUrl ->
                                        val fullIconUrl = getFullIconUrl(iconUrl) ?: ""

                                        Box(
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                        ) {
                                            KamelImage(
                                                resource = asyncPainterResource(fullIconUrl),
                                                contentDescription = transformation.name,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(8.dp),
                                                onLoading = {
                                                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                                                },
                                                onFailure = {
                                                    Text("⚠️", Modifier.align(Alignment.Center))
                                                }
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    Text(
                                        text = transformation.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    transformation.health?.let {
                                        Text(
                                            text = "Health: $it",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    transformation.movementSpeed?.let {
                                        Text(
                                            text = "Speed: $it",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // No mostramos skins/costumes como se solicitó
        // La sección de costumes ha sido completamente eliminada

        // Habilidades
        character.abilities?.let { abilities ->
            if (abilities.isNotEmpty()) {
                item {
                    Text(
                        text = "Abilities",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Agrupar habilidades por transformation_id
                val abilityGroups = abilities.groupBy { it.transformationId }
                val transformationMap = character.transformations?.associateBy { it.id } ?: emptyMap()

                abilityGroups.forEach { (transformationId, abilitiesForTransformation) ->
                    item {
                        transformationId?.let {
                            transformationMap[it]?.let { transformation ->
                                Text(
                                    text = "Form: ${transformation.name}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            abilitiesForTransformation.filter { it.name != null }.forEach { ability ->
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        ability.icon?.let { iconUrl ->
                                            val fullIconUrl = getFullIconUrl(iconUrl) ?: ""

                                            Box(
                                                modifier = Modifier
                                                    .size(50.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                            ) {
                                                KamelImage(
                                                    resource = asyncPainterResource(fullIconUrl),
                                                    contentDescription = ability.name,
                                                    contentScale = ContentScale.Fit,
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(4.dp),
                                                    onLoading = {
                                                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                                                    },
                                                    onFailure = {
                                                        Text("⚠️", Modifier.align(Alignment.Center))
                                                    }
                                                )
                                            }
                                        }

                                        Column(Modifier.weight(1f)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    text = ability.name ?: "",
                                                    style = MaterialTheme.typography.titleMedium
                                                )

                                                ability.type?.let {
                                                    val typeColor = when (it) {
                                                        "Ultimate" -> Color(0xFFFF5722)
                                                        "Weapon" -> Color(0xFF673AB7)
                                                        else -> Color(0xFF03A9F4)
                                                    }

                                                    Surface(
                                                        shape = RoundedCornerShape(4.dp),
                                                        color = typeColor,
                                                        contentColor = Color.White,
                                                        modifier = Modifier.padding(horizontal = 4.dp)
                                                    ) {
                                                        Text(
                                                            text = it,
                                                            style = MaterialTheme.typography.labelSmall,
                                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            ability.description?.let {
                                                Text(
                                                    text = it,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }

                                            ability.additionalFields?.let { fields ->
                                                if (fields.isNotEmpty()) {
                                                    Card(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        colors = CardDefaults.cardColors(
                                                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                                        )
                                                    ) {
                                                        Column(
                                                            modifier = Modifier.padding(8.dp),
                                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                                        ) {
                                                            fields.forEach { (key, value) ->
                                                                Row(
                                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                                ) {
                                                                    Text(
                                                                        text = "$key:",
                                                                        style = MaterialTheme.typography.bodySmall,
                                                                        fontWeight = FontWeight.Bold,
                                                                        modifier = Modifier.width(100.dp)
                                                                    )

                                                                    Text(
                                                                        text = value,
                                                                        style = MaterialTheme.typography.bodySmall,
                                                                        modifier = Modifier.weight(1f)
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}