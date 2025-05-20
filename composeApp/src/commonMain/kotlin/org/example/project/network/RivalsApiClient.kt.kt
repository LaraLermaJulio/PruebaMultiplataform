package org.example.project.network
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.data.RivalsCharacter

object RivalsApiClient {
    private const val BASE_URL = "https://marvelrivalsapi.com/api/v1/"

    // Reemplaza esto con tu API key
    private const val API_KEY = "c9bcc04a9b6efa61795f220f91ea85812a5041488fe3109c1e215aa7ddca77b3"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    // Personajes de demostración en caso de error
    private val demoCharacters = listOf(
        RivalsCharacter(
            id = "1011",
            name = "Hulk",
            realName = "Bruce Banner",
            imageUrl = "/rivals/heroes/card/hulk.png",
            role = "Vanguard",
            attackType = "Melee Heroes",
            team = listOf("Avengers"),
            difficulty = "4",
            bio = "Brilliant scientist Dr. Bruce Banner has finally found a way to coexist with his monstrous alter ego, the Hulk. By accumulating gamma energy over transformations, he can become a wise and strong Hero Hulk or a fierce and destructive Monster Hulk – a true force of fury on the battlefield!",
            lore = "Caught in the detonation of a powerful weapon of his own invention, Dr. Bruce Banner absorbed gamma radiation that transforms him into a massive green monster whenever his emotions rage out of control. As Banner, he's still a genius. But as the Hulk, he's the strongest one there is!\nBanner developed a special Gamma Belt to control his transformations and temper the Hulk's fury. But when the Timestream Entanglement transformed Los Diablos Missile Base into a demonic battleground, he realized that the best way to fight monsters was to let out the one within."
        )
    )

    suspend fun fetchCharacters(): List<RivalsCharacter> {
        return try {
            val response = client.get("${BASE_URL}heroes") {
                headers {
                    append("x-api-key", API_KEY)
                }
            }
            response.body<List<RivalsCharacter>>()
        } catch (e: Exception) {
            println("Error fetching Marvel Rivals characters: ${e.message}")
            // En caso de error, devolver personajes de demostración
            demoCharacters
        }
    }
}