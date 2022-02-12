package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.Tournament]
 */
@Serializable
sealed class TournamentAction : ContextAction() {
    override val context: Context = Context.Tournament

    /**
     * Adds a new tournament. A successful response to this would be [TournamentReaction.Added]
     */
    @Serializable
    data class Add(val name: String) : TournamentAction() {
        override val action: Action = Action.Add
        override val minimumAccessLevel: Int = 2
    }

    /**
     * Removes the tournament with id [tournamentId]. The tournament must not be associated with any teams, games or
     * alike. A successful response to this would be [TournamentReaction.Removed]
     */
    @Serializable
    data class Remove(val tournamentId: Int) : TournamentAction() {
        override val action: Action = Action.Remove
        override val minimumAccessLevel: Int = 2
    }

    /**
     * Fetches the tournament with id [tournamentId] or all tournaments if it's null. A successful response to this
     * would be [TournamentReaction.Fetched]
     */
    @Serializable
    data class Fetch(val tournamentId: Int? = null) : TournamentAction() {
        override val action: Action = Action.Fetch
        override val minimumAccessLevel: Int? = null
    }
}
