package org.example.project.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarvelResponse(
    val code: Int,
    val status: String? = null,
    val copyright: String? = null,
    val attributionText: String? = null,
    val data: MarvelData
)

@Serializable
data class MarvelData(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<MarvelCharacter>
)