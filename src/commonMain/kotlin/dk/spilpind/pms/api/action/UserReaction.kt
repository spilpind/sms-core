package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction

/**
 * All possible reactions that can be made in relation to [Context.User]
 */
enum class UserReaction(val reaction: Reaction) {
    Added(Reaction.Added),
    Removed(Reaction.Removed),
    Fetched(Reaction.Fetched),
}
