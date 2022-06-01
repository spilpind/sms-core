package dk.spilpind.sms.api.action

import dk.spilpind.sms.core.model.Context
import kotlinx.serialization.Serializable

/**
 * Interface for all reactions for specific contexts
 */
@Serializable
sealed class ContextReaction : ReactionData() {
    abstract val context: Context
}
