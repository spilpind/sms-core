package dk.spilpind.sms.api.action

import dk.spilpind.sms.core.model.Context
import dk.spilpind.sms.api.common.Reaction
import dk.spilpind.sms.core.GameHelper
import dk.spilpind.sms.core.model.Event.Raw
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.Referee]
 */
@Serializable
sealed class RefereeReaction : ContextReaction() {
    override val context: Context = Context.Referee

    /**
     * Response to changes in the game, e.g. [RefereeAction.Add], [RefereeAction.Remove] (both directly and via
     * subscriptions). [gameState] matches [GameHelper.GameState.name]. [inTeamId] and [outTeamId] is always non-null
     * except if [gameState] is [GameHelper.GameState.NOT_STARTED]. [faults] is faults for the current player (since
     * last death/points/switch) and [deaths] is deaths of current in team (since last switch). [gameTime] and
     * [turnTime] is in seconds and represent time since start of the game and time since last switch (both excluding
     * any pauses in the game). [recentEvents] is the most recent events and ordered such that the newest event is first
     */
    @Serializable
    data class Updated(
        val gameId: Int,
        val gameState: String,
        val inTeamId: Int?,
        val outTeamId: Int?,
        val inTeamPoints: Int,
        val outTeamPoints: Int,
        val faults: Int,
        val deaths: Int,
        val gameTime: Int,
        val turnTime: Int,
        val liftSucceeded: Boolean,
        val recentEvents: Collection<Event>
    ) : RefereeReaction() {
        override val reaction: Reaction = Reaction.Updated
    }

    /**
     * Response to [RefereeAction.Subscribe]
     */
    @Serializable
    object Subscribed : RefereeReaction() {
        override val reaction: Reaction = Reaction.Subscribed
    }

    /**
     * Response to [RefereeAction.Unsubscribe]
     */
    @Serializable
    object Unsubscribed : RefereeReaction() {
        override val reaction: Reaction = Reaction.Unsubscribed
    }

    /**
     * Represents a single event. For more info, see [Raw]
     */
    @Serializable
    data class Event(
        val eventId: Int,
        val teamId: Int?,
        val typeId: Int,
        val time: Int,
        val refereeId: Int,
        val created: LocalDateTime,
        val points: Int? = null
    )
}
