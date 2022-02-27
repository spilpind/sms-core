package dk.spilpind.pms.api

import dk.spilpind.pms.api.action.ReactionData
import kotlinx.serialization.Serializable

/**
 * Data needed in order to make a response from the api
 */
@Serializable
data class Response(
    val context: String?,
    val reaction: String,
    val actionId: String?,
    val data: ReactionData
)
