package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.core.model.Context
import dk.spilpind.sms.core.model.Event
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.Referee]
 */
@Serializable
sealed class RefereeAction : ContextAction() {
    override val context: Context = Context.Referee

    /**
     * Adds an event to the game identified by [gameId]. [typeId] should match the core type ids (see [Event.typeId]).
     * [lastEventId] ensures the client has the newest event locally and hasn't come out of sync. [startingInTeam] is
     * only required to be non-null for [Event.Timing.TimingType.GameStart] and [points] is only required to be non-null
     * for [Event.Points]. Depending on state of the game (see [RefereeReaction.Updated.gameState]) only certain event
     * types are allowed to add. A successful response to this would be [RefereeReaction.Updated]
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
     * Subscribes to the game identified by [gameId]. The subscription will be kept alive until the session is destroyed
     * or [Unsubscribe] is called. Changes to the game will be sent via relevant [RefereeReaction]s. A successful
     * response to this would be [RefereeReaction.Subscribed] followed by [RefereeReaction.Updated]
     */
    @Serializable
    data class Subscribe(val gameId: Int) : RefereeAction() {
        override val action: Action = Action.Subscribe
        override val minimumAccessLevel: Int? = null
    }

    /**
     * Unsubscribes the socket from the game identified by [gameId], previously subscribed by [Subscribe]. A successful
     * response to this would be [RefereeReaction.Unsubscribed]
     */
    @Serializable
    data class Unsubscribe(val gameId: Int) : RefereeAction() {
        override val action: Action = Action.Unsubscribe
        override val minimumAccessLevel: Int? = null
    }
}
