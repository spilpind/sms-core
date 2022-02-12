package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.Referee]
 */
sealed class RefereeAction : ContextAction {
    override val context: Context = Context.Referee

    /**
     * Adds an event to the game identified by [gameId]. [lastEventId] ensures the client has the newest event locally
     * (to make sure it's not out of sync). [typeId] should match the core type ids and the remaining parameters can be
     * left out dependent on the type of event. A successful response to this would be [RefereeReaction.Updated]
     */
    @Serializable
    data class Add(
        val gameId: Int,
        val typeId: Int,
        val lastEventId: Int?,
        val startingInTeam: Int? = null,
        val points: Int? = null
    ) : RefereeAction() {
        override val action: Action = Action.Add
        override val minimumAccessLevel: Int = 2
    }

    /**
     * Removes the event identified by [eventId]. This has to be the last event in the game. A successful response to
     * this would be [RefereeReaction.Updated]
     */
    @Serializable
    data class Remove(val eventId: Int) : RefereeAction() {
        override val action: Action = Action.Remove
        override val minimumAccessLevel: Int = 2
    }

    /**
     * Subscribes the socket to the game identified by [gameId]. The subscription will be kept alive until the socket
     * disconnects or [Unsubscribe] is called. Changes to the game will be sent to the socket, most likely via
     * [RefereeReaction.Updated]. It is allowed to have subscriptions to several games at the same time. A successful
     * response to this would be [RefereeReaction.Subscribed]
     */
    @Serializable
    data class Subscribe(val gameId: Int) : RefereeAction() {
        override val action: Action = Action.Subscribe
        override val minimumAccessLevel: Int? = null
    }

    /**
     * Unsubscribes the socket from the game identified by [gameId], previously subscribed by [Subscribe].  A successful
     * response to this would be [RefereeReaction.Unsubscribed]
     */
    @Serializable
    data class Unsubscribe(val gameId: Int) : RefereeAction() {
        override val action: Action = Action.Unsubscribe
        override val minimumAccessLevel: Int? = null
    }
}
