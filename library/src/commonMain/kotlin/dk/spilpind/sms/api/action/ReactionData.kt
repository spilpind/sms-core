package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Reaction
import kotlinx.serialization.Serializable

/**
 * Base of all reactions, including non-successful
 */
@Serializable
sealed class ReactionData {
    abstract val reaction: Reaction
}
