package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Reaction

enum class AuthenticationReaction(val reaction: Reaction) {
    Informed(Reaction.Informed),
    Fetched(Reaction.Fetched),
}

