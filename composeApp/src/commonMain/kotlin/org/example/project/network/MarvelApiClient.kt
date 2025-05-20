package org.example.project.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.datetime.Clock
import okio.ByteString
import okio.ByteString.Companion.encodeUtf8
import okio.ByteString.Companion.toByteString
import org.example.project.data.MarvelCharacter
import org.example.project.data.MarvelResponse

object MarvelApiClient {
    private const val BASE_URL = "https://gateway.marvel.com/v1/public/"

    // Tus claves Marvel API
    private const val PUBLIC_KEY = "acdb1fae4330cfa32d624df36609526a"
    private const val PRIVATE_KEY = "801449dcb190aa4f51c927b1fd14ea26efe4e7f7"

    private fun getTimestamp(): String {
        return Clock.System.now().toEpochMilliseconds().toString()
    }

    private fun generateHash(ts: String): String {
        val input = "$ts$PRIVATE_KEY$PUBLIC_KEY"
        return input.encodeUtf8().md5().hex()
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    private val demoCharacters = listOf(
        MarvelCharacter(
            id = 1009220,
            name = "Captain America",
            description = "Recipient of the Super-Soldier serum...",
            thumbnail = org.example.project.data.Thumbnail(
                path = "https://i.annihil.us/u/prod/marvel/i/mg/3/50/537ba56d31087",
                extension = "jpg"
            )
        ),
        MarvelCharacter(
            id = 1009368,
            name = "Iron Man",
            description = "Wounded, captured and forced to build a weapon...",
            thumbnail = org.example.project.data.Thumbnail(
                path = "https://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55",
                extension = "jpg"
            )
        ),
        MarvelCharacter(
            id = 1009664,
            name = "Thor",
            description = "As the Norse God of thunder and lightning...",
            thumbnail = org.example.project.data.Thumbnail(
                path = "https://i.annihil.us/u/prod/marvel/i/mg/d/d0/5269657a74350",
                extension = "jpg"
            )
        )
        // Puedes agregar más personajes si lo deseas
    )

    suspend fun fetchCharacters(offset: Int = 0, limit: Int = 20): List<MarvelCharacter> {
        return try {
            val timestamp = getTimestamp()
            val hash = generateHash(timestamp)
            println("✅ Timestamp: $timestamp")
            println("✅ Hash: $hash")
            println("✅ URL: https://gateway.marvel.com/v1/public/characters?apikey=$PUBLIC_KEY&ts=$timestamp&hash=$hash")
            val response = client.get("${BASE_URL}characters") {
                parameter("apikey", PUBLIC_KEY)
                parameter("ts", timestamp)
                parameter("hash", hash)
                parameter("offset", offset)
                parameter("limit", limit)
                parameter("orderBy", "name")
            }


            response.body<MarvelResponse>().data.results
        } catch (e: Exception) {
            println("Error fetching Marvel characters: ${e.message}")
            if (e.message?.contains("InvalidCredentials") == true) {
                println("❌ API key inválida. Revisa que esté correctamente copiada desde https://developer.marvel.com")
            }
            if (offset == 0) demoCharacters.take(3) else emptyList()
        }
    }
}
