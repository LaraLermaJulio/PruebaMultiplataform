package org.example.project


/**
 * Construye la URL completa para una imagen
 * Si la URL ya comienza con http, la devuelve tal cual
 * Si no, añade el prefijo de la API
 */
fun getFullImageUrl(imageUrl: String): String {
    return if (imageUrl.startsWith("http")) {
        imageUrl
    } else {
        "https://marvelrivalsapi.com$imageUrl"
    }
}

/**
 * Construye la URL completa para un icono
 * Igual que getFullImageUrl pero específicamente para iconos
 */
fun getFullIconUrl(iconUrl: String?): String? {
    return iconUrl?.let {
        getFullImageUrl(it)
    }
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