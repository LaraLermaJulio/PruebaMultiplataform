package org.example.project.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RivalsCharacter(
    val id: String,
    val name: String,
    @SerialName("real_name") val realName: String? = null,
    val imageUrl: String,
    val role: String? = null,
    @SerialName("attack_type") val attackType: String? = null,
    val team: List<String>? = null,
    val difficulty: String? = null,
    val bio: String? = null,
    val lore: String? = null,
    val transformations: List<Transformation>? = null
) {
    @Serializable
    data class Transformation(
        val id: String,
        val name: String,
        val icon: String? = null,
        val health: String? = null,
        @SerialName("movement_speed") val movementSpeed: String? = null
    )
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