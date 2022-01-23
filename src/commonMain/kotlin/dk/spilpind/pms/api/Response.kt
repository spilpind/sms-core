package dk.spilpind.pms.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Response(val reaction: String, val actionId: String?, val data: JsonObject)
