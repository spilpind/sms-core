package dk.spilpind.sms.api

import dk.spilpind.sms.api.action.ReactionData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data needed in order to make a response from the api. [context] and [reaction] in combination define what kind of
 * response this is and [data] has to match data for that response type (see subclasses of [ReactionData]). [context]
 * does in most cases match [Request.context], but that's not always the case - especially in error cases. [actionId]
 * matches [Request.actionId] (if it could be deducted). Note that one request could end up with several responses with
 * the same action id
 */
@Serializable
data class Response(
    @SerialName(CONTEXT_SERIAL_NAME)
    val context: String?,
    @SerialName(REACTION_SERIAL_NAME)
    val reaction: String,
    val actionId: String?,
    @SerialName(DATA_SERIAL_NAME)
    val data: ReactionData
) {
    companion object {
        internal const val CONTEXT_SERIAL_NAME = "context"
        internal const val REACTION_SERIAL_NAME = "reaction"
        internal const val DATA_SERIAL_NAME = "data"
    }
}
