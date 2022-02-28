package dk.spilpind.pms.api.action

import kotlinx.serialization.Serializable

/**
 * Interface for all reactions for specific contexts
 */
@Serializable
sealed class ContextReaction : ReactionData()
