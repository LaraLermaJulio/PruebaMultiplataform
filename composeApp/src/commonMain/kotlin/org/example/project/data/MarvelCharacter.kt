package org.example.project.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarvelCharacter(
    val id: Int,
    val name: String,
    val description: String,
    @SerialName("thumbnail") val thumbnail: Thumbnail,
    val comics: ResourceList? = null,
    val series: ResourceList? = null,
    val stories: ResourceList? = null,
    val events: ResourceList? = null,
    @SerialName("modified") val modified: String? = null
)

@Serializable
data class Thumbnail(
    val path: String,
    val extension: String
) {
    fun getUrl(): String {
        // Asegurar que usamos HTTPS ya que algunas URLs vendrán con HTTP y causarán problemas
        val securePath = if (path.startsWith("http:")) {
            path.replace("http:", "https:")
        } else {
            path
        }
        return "$securePath.$extension"
    }
}

@Serializable
data class ResourceList(
    val available: Int? = null,
    val returned: Int? = null,
    val collectionURI: String? = null,
    val items: List<ResourceItem>? = null
)

@Serializable
data class ResourceItem(
    val resourceURI: String? = null,
    val name: String? = null,
    val type: String? = null
)