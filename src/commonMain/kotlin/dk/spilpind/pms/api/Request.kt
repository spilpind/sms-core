package dk.spilpind.pms.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Request(
    val context: String? = null,
    val action: String? = null,
    val actionId: String? = null,
    val jsonWebToken: String? = null,
    val data: JsonObject
)
