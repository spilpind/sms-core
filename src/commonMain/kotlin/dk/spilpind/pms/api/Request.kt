package dk.spilpind.pms.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Data needed in order to make a request to the api
 */
@Serializable
data class Request(
    val context: String? = null,
    val action: String? = null,
    val actionId: String? = null,
    val jsonWebToken: String? = null,
    val data: JsonObject
)
