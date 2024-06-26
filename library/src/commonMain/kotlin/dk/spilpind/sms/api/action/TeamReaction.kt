package dk.spilpind.sms.api.action

import dk.spilpind.sms.core.model.Context
import dk.spilpind.sms.api.common.Reaction
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
     * Response to changes in one or more of the teams. If [allTeams] is false, only the listed teams should be updated
     * - if it's true, the entire list should be replaced
     */
    @Serializable
    data class Updated(
        val allTeams: Boolean,
        val teams: List<Team>
    ) : TeamReaction() {
        override val reaction: Reaction = Reaction.Updated
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
     * Response to [TeamAction.CreateRequest]. [code] will represent the newly generated code that should be used for
     * accepting the request
     */
    @Serializable
    data class RequestCreated(val code: String) : TeamReaction() {
        override val reaction: Reaction = Reaction.RequestCreated
    }

    /**
     * Response to [TeamAction.RevokeRequest]
     */
    @Serializable
    class RequestRevoked : TeamReaction() {
        override val reaction: Reaction = Reaction.RequestRevoked
    }

    /**
     * Response to [TeamAction.AcceptRequest]. [team] will represent the team which the pending request was related to
     */
    @Serializable
    data class RequestAccepted(val team: Team) : TeamReaction() {
        override val reaction: Reaction = Reaction.RequestAccepted
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
