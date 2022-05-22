package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Context
import dk.spilpind.sms.api.common.Reaction
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.Referee]
 */
@Serializable
sealed class RefereeReaction : ContextReaction() {
    override val context: Context = Context.Referee

    /**
     * Response to changes in the game, e.g. [RefereeAction.Add] and [RefereeAction.Remove]. Depending on [gameState]
     * and a few other parameters only certain event types are allowed to add to the game. [recentEvents] are ordered
     * such that the newest event is first
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
        val dead: Int,
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
     * Represents a single event
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