package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Reaction

enum class RefereeReaction(val reaction: Reaction) {
    Updated(Reaction.Updated),
    Subscribed(Reaction.Subscribed),
    Unsubscribed(Reaction.Unsubscribed)
}

