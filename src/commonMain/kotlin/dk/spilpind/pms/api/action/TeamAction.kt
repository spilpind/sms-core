package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.Team]
 */
@Serializable
sealed class TeamAction : ContextAction() {
    override val context: Context = Context.Team

    /**
     * Adds a new team. A successful response to this would be [TeamReaction.Added]
     */
    @Serializable
    data class Add(
        val name: String,
        val shortName: String,
        val tournamentId: Int,
        val addAsCaptain: Boolean = false
    ) : TeamAction() {
        override val action: Action = Action.Add
        override val minimumAccessLevel: Int = 2
    }

    /**
     * Removes the team with id [teamId]. The team cannot be used for any games or alike. A successful response to this
     * would be [TeamReaction.Removed]
     */
    @Serializable
    data class Remove(val teamId: Int) : TeamAction() {
        override val action: Action = Action.Remove
        override val minimumAccessLevel: Int = 2
    }

    /**
     * Fetches the team with id [teamId] or all teams if it's null. A successful response to this would be
     * [TeamReaction.Fetched]
     */
    @Serializable
    data class Fetch(val teamId: Int? = null) : TeamAction() {
        override val action: Action = Action.Fetch
        override val minimumAccessLevel: Int? = null
    }

    /**
     * Subscribes the socket to all teams available for the current user and associated with the tournament identified
     * by [tournamentId]. The subscription will be kept alive until the socket disconnects or [Unsubscribe] is called.
     * Changes to the list will be sent to the socket, via relevant [TeamReaction]s. A successful response to this would
     * be [TeamReaction.Subscribed] followed by [TeamReaction.Updated]
     */
    @Serializable
    data class Subscribe(val tournamentId: Int) : TeamAction() {
        override val action: Action = Action.Subscribe
        override val minimumAccessLevel: Int? = null
    }

    /**
     * Unsubscribes the socket from updates to the list of teams, previously subscribed by [Subscribe]. A successful
     * response to this would be [TeamReaction.Unsubscribed]
     */
    @Serializable
    data class Unsubscribe(val tournamentId: Int) : TeamAction() {
        override val action: Action = Action.Unsubscribe
        override val minimumAccessLevel: Int? = null
    }
}
