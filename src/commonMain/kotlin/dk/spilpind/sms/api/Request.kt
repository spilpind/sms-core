package dk.spilpind.sms.api

import dk.spilpind.sms.api.action.ContextAction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data needed in order to make a request to the api
 */
@Serializable
data class Request(
    @SerialName(CONTEXT_SERIAL_NAME)
    val context: String? = null,
    @SerialName(ACTION_SERIAL_NAME)
    val action: String? = null,
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
