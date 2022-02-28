package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import kotlinx.serialization.Serializable

/**
 * Interface for all reactions for specific contexts
 */
@Serializable
sealed class ContextReaction : ReactionData() {
    abstract val context: Context
}
