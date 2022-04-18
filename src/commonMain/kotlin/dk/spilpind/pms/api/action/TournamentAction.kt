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

    /**
     * Subscribes the socket to all tournaments available for the current user. The subscription will be kept alive
     * until the socket disconnects or [Unsubscribe] is called. Changes to the list will be sent to the socket, via
     * [TournamentReaction.Updated]. A successful response to this would be [TournamentReaction.Subscribed] followed by
     * [TournamentReaction.Updated]
     */
    @Serializable
    class Subscribe : TournamentAction() {
        override val action: Action = Action.Subscribe
        override val minimumAccessLevel: Int? = null
    }

    /**
     * Unsubscribes the socket from updates to the list of tournaments, previously subscribed by [Subscribe]. A
     * successful response to this would be [TournamentReaction.Unsubscribed]
     */
    @Serializable
    class Unsubscribe : TournamentAction() {
        override val action: Action = Action.Unsubscribe
        override val minimumAccessLevel: Int? = null
    }
}
