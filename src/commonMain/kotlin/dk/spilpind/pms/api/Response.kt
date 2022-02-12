package dk.spilpind.pms.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Data needed in order to make a response from the api
 */
@Serializable
data class Response(
    val context: String?,
    val reaction: String,
    val actionId: String?,
    val data: JsonObject
)
