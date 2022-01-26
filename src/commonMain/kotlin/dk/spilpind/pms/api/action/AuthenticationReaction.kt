package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction

/**
 * All possible reactions that can be made in relation to [Context.Authentication]
 */
enum class AuthenticationReaction(val reaction: Reaction) {
    Informed(Reaction.Informed),
    Fetched(Reaction.Fetched),
}
