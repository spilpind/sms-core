package dk.spilpind.pms.api

import dk.spilpind.pms.api.action.ContextAction
import kotlinx.serialization.Serializable

/**
 * Data needed in order to make a request to the api
 */
@Serializable
data class Request(
    val context: String? = null,
    val action: String? = null,
    val actionId: String? = null,
    val data: ContextAction
)
