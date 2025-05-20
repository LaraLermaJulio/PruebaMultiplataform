package org.example.project

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Construye la URL completa para una imagen
 * Si la URL ya comienza con http, la devuelve tal cual
 * Si no, añade el prefijo de la API
 */
fun getFullImageUrl(imageUrl: String): String {
    // Log para depuración
    println("Original imageUrl: $imageUrl")

    // Si ya es una URL completa, devolverla tal cual
    if (imageUrl.startsWith("http")) {
        return imageUrl
    }

    // Si no tiene barra inicial, añadirla
    val normalizedUrl = if (!imageUrl.startsWith("/")) {
        "/$imageUrl"
    } else {
        imageUrl
    }

    // Devolver la URL completa con el prefijo de la API
    val fullUrl = "https://marvelrivalsapi.com$normalizedUrl"
    println("Constructed full URL: $fullUrl")
    return fullUrl
}

/**
 * Construye la URL completa para un icono
 * Mejorada para manejar los paths específicos de los iconos
 */
fun getFullIconUrl(iconUrl: String?): String? {
    if (iconUrl == null) return null

    println("Original iconUrl: $iconUrl")

    // Si ya es una URL completa, devolverla tal cual
    if (iconUrl.startsWith("http")) {
        return iconUrl
    }

    // Verificar si el path está en formato /abilities/, /heroes/transformations/, etc.
    val normalizedUrl = if (!iconUrl.startsWith("/")) {
        "/$iconUrl"
    } else {
        iconUrl
    }

    // Construir URL completa
    val fullUrl = "https://marvelrivalsapi.com$normalizedUrl"
    println("Constructed icon URL: $fullUrl")
    return fullUrl
}

/**
 * Obtiene el color apropiado para la calidad de un traje
 * @param quality Calidad del traje (BLUE, PURPLE, ORANGE, etc.)
 * @return Código de color hexadecimal para usar en la UI
 */
fun getQualityColor(quality: String?): Long {
    return when (quality) {
        "BLUE" -> 0xFF3D85C6
        "PURPLE" -> 0xFF8A2BE2
        "ORANGE" -> 0xFFFF6600
        "RED" -> 0xFFCC0000
        "GOLD" -> 0xFFFFD700
        else -> 0xFF888888
    }
}

/**
 * Obtiene el color para un tipo de habilidad
 * @param type Tipo de habilidad (Ultimate, Weapon, etc.)
 * @return Código de color hexadecimal para usar en la UI
 */
fun getAbilityTypeColor(type: String?): Long {
    return when (type) {
        "Ultimate" -> 0xFFFF5722
        "Weapon" -> 0xFF673AB7
        "Special" -> 0xFF03A9F4
        "Passive" -> 0xFF4CAF50
        else -> 0xFF2196F3
    }
}