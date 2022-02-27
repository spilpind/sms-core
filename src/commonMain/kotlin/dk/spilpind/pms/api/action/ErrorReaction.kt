package dk.spilpind.pms.api.action

import kotlinx.serialization.Serializable

/**
 * Error class used as reaction to e.g. invalid actions
 */
@Serializable
data class ErrorReaction(val action: String?, val message: String?) : ReactionData()
