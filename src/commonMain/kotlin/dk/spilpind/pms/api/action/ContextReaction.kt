package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Reaction

/**
 * Interface for all reactions for specific contexts
 */
sealed interface ContextReaction {
    val reaction: Reaction
}
