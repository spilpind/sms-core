package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction

/**
 * All possible reactions that can be made in relation to [Context.Referee]
 */
enum class RefereeReaction(val reaction: Reaction) {
    Updated(Reaction.Updated),
    Subscribed(Reaction.Subscribed),
    Unsubscribed(Reaction.Unsubscribed)
}
