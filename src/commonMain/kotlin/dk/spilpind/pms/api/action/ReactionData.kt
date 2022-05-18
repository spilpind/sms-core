package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Reaction
import kotlinx.serialization.Serializable

@Serializable
sealed class ReactionData {
    abstract val reaction: Reaction
}
