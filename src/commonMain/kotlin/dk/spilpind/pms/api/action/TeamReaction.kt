package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction
import dk.spilpind.pms.api.data.FetchedData
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.Team]
 */
@Serializable
sealed class TeamReaction : ContextReaction() {
    override val context: Context = Context.Team

    /**
     * Response to [TeamAction.Add]
     */
    @Serializable
    data class Added(val team: Team) : TeamReaction() {
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
     * Response to changes in one or more of the teams
     */
    @Serializable
    data class Updated(
        val allItems: Boolean,
        val items: List<Team>
    ) : TeamReaction() {
        override val reaction: Reaction = Reaction.Updated
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
     * Response to [TeamAction.Subscribe]
     */
    @Serializable
    class Subscribed : TeamReaction() {
        override val reaction: Reaction = Reaction.Subscribed
    }

    /**
     * Response to [TeamAction.Unsubscribe]
     */
    @Serializable
    class Unsubscribed : TeamReaction() {
        override val reaction: Reaction = Reaction.Unsubscribed
    }

    /**
     * Represents a single team
     */
    @Serializable
    data class Team(
        val teamId: Int,
        val name: String,
        val shortName: String,
        val tournamentId: Int
    )
}
