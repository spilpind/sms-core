package dk.spilpind.pms.api.data

import kotlinx.serialization.Serializable

/**
 * Error class used as reaction to e.g. invalid actions
 */
@Serializable
data class ErrorData(val action: String?, val message: String?)
