package dk.spilpind.sms.api

import dk.spilpind.sms.api.action.ContextAction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data needed in order to make a request to the api. [context] and [action] in combination define what kind of request
 * this is and [data] has to match data for that request type (see subclasses of [ContextAction]). [actionId] is useful
 * for the client to keep track of what responses match this request (see [Response.actionId]), but might be left out of
 * some responses, e.g. in case of a fatal data structure error
 */
@Serializable
data class Request(
    @SerialName(CONTEXT_SERIAL_NAME)
    val context: String,
    @SerialName(ACTION_SERIAL_NAME)
    val action: String,
    @SerialName(ACTION_ID_SERIAL_NAME)
    val actionId: String? = null,
    @SerialName(DATA_SERIAL_NAME)
    val data: ContextAction
) {
    companion object {
        internal const val CONTEXT_SERIAL_NAME = "context"
        internal const val ACTION_SERIAL_NAME = "action"
        internal const val ACTION_ID_SERIAL_NAME = "actionId"
        internal const val DATA_SERIAL_NAME = "data"
    }
}
