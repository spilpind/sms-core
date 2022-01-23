package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Reaction

enum class TournamentReaction(val reaction: Reaction) {
    Added(Reaction.Added),
    Removed(Reaction.Removed),
    Fetched(Reaction.Fetched),
}

