package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction
import dk.spilpind.pms.api.data.FetchedData
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.Team]
 */
sealed class TeamReaction : ContextReaction() {

    /**
     * Response to [TeamAction.Add]
     */
    @Serializable
    data class Added(
        val teamId: Int,
        val name: String,
        val shortName: String,
        val tournament: Tournament
    ) : TeamReaction() {
        override val reaction: Reaction = Reaction.Added
    }

    /**
     * Response to [TeamAction.Remove]
     */
    @Serializable
    data class Removed(val teamId: Int) : TeamReaction() {
        override val reaction: Reaction = Reaction.Removed
    }

    /**
     * Response to [TeamAction.Fetch]. If the request specified a specific team id, [items] will contain that team as
     * the only item
     */
    @Serializable
    data class Fetched(override val items: List<Team>) : TeamReaction(), FetchedData<Team> {
        override val reaction: Reaction = Reaction.Fetched
    }

    /**
     * Represents a single game
     */
    @Serializable
    data class Team(
        val teamId: Int,
        val name: String,
        val shortName: String,
        val tournament: Tournament
    )

    /**
     * Represents a single tournament
     */
    @Serializable
    data class Tournament(val tournamentId: Int, val name: String)
}
