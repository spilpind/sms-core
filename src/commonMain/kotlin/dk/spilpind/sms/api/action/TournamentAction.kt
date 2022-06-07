package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.core.model.Context
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
    }

    /**
     * Removes the tournament with id [tournamentId]. The tournament must not be associated with any teams, games or
     * alike. A successful response to this would be [TournamentReaction.Removed]
     */
    @Serializable
    data class Remove(val tournamentId: Int) : TournamentAction() {
        override val action: Action = Action.Remove
    }

    /**
     * Subscribes to all tournaments available for the current user. The subscription will be kept alive until the
     * session is destroyed or [Unsubscribe] is called. Changes to the list will be sent via relevant
     * [TournamentReaction]s. A successful response to this would be [TournamentReaction.Subscribed] followed by
     * [TournamentReaction.Updated]
     */
    @Serializable
    class Subscribe : TournamentAction() {
        override val action: Action = Action.Subscribe
    }

    /**
     * Unsubscribes the socket from updates to the list of tournaments, previously subscribed by [Subscribe]. A
     * successful response to this would be [TournamentReaction.Unsubscribed]
     */
    @Serializable
    class Unsubscribe : TournamentAction() {
        override val action: Action = Action.Unsubscribe
    }
}
