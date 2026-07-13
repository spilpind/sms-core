package dk.spilpind.sms.api.action

import dk.spilpind.sms.core.model.Context
import dk.spilpind.sms.api.common.Reaction
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.GameGrouping]
 */
@Serializable
sealed class GameGroupingReaction : ContextReaction() {
    override val context: Context = Context.GameGrouping

    /**
     * Response to [GameGroupingAction.Add] and updates via subscriptions
     */
    @Serializable
    data class Added(val gameGrouping: GameGrouping) : GameGroupingReaction() {
        override val reaction: Reaction = Reaction.Added
    }

    /**
     * Response to [GameGroupingAction.Remove] and updates via subscriptions
     */
    @Serializable
    data class Removed(val gameGroupingId: Int) : GameGroupingReaction() {
        override val reaction: Reaction = Reaction.Removed
    }

    /**
     * Response to changes in one or more of the game groupings. If [allGameGroupings] is false, only the listed game
     * groupings should be updated - if it's true, the entire list should be replaced
     */
    @Serializable
    data class Updated(
        val allGameGroupings: Boolean,
        val gameGroupings: List<GameGrouping>
    ) : GameGroupingReaction() {
        override val reaction: Reaction = Reaction.Updated
    }

    /**
     * Response to [GameGroupingAction.Subscribe]
     */
    @Serializable
    class Subscribed : GameGroupingReaction() {
        override val reaction: Reaction = Reaction.Subscribed
    }

    /**
     * Response to [GameGroupingAction.Unsubscribe]
     */
    @Serializable
    class Unsubscribed : GameGroupingReaction() {
        override val reaction: Reaction = Reaction.Unsubscribed
    }

    /**
     * Represents a single game grouping
     */
    @Serializable
    data class GameGrouping(
        val gameGroupingId: Int,
        val tournamentId: Int,
        val name: String,
        val abbreviation: String?,
        val level: Int,
        val gameRulesId: Int?,
    )
}
