package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.core.model.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.GameGrouping]
 */
@Serializable
sealed class GameGroupingAction : ContextAction() {
    override val context: Context = Context.GameGrouping

    /**
     * Adds a new game grouping associated with the tournament identified by [tournamentId]. [level] describes how far
     * into the tournament the grouping is, where a lower value is more important (e.g. 1 represents the final).
     * [gameRulesId] optionally points to the game rules applied to games in the grouping that don't specify their own.
     * A successful response to this would be [GameGroupingReaction.Added]
     */
    @Serializable
    data class Add(
        val tournamentId: Int,
        val name: String,
        val abbreviation: String? = null,
        val level: Int,
        val gameRulesId: Int? = null,
    ) : GameGroupingAction() {
        override val action: Action = Action.Add
    }

    /**
     * Removes the game grouping with id [gameGroupingId]. A successful response to this would be
     * [GameGroupingReaction.Removed]
     */
    @Serializable
    data class Remove(val gameGroupingId: Int) : GameGroupingAction() {
        override val action: Action = Action.Remove
    }

    /**
     * Updates the game grouping with id [gameGroupingId] with the values provided. It is important to consider all
     * values even though you're not allowed to change them, as for instance a value set to empty or null will be
     * updated to that value
     */
    @Serializable
    data class Update(
        val gameGroupingId: Int,
        val name: String,
        val abbreviation: String?,
        val level: Int,
        val gameRulesId: Int?,
    ) : GameGroupingAction() {
        override val action: Action = Action.Update
    }

    /**
     * Subscribes either to all game groupings associated with the specific [tournamentId] or the single game grouping
     * associated with [gameGroupingId] (both cannot be specified). The subscription will be kept alive until the
     * session is destroyed or [Unsubscribe] is called. Changes to the list will be sent via relevant
     * [GameGroupingReaction]s. A successful response to this would be [GameGroupingReaction.Subscribed] followed by
     * [GameGroupingReaction.Updated]
     */
    @Serializable
    data class Subscribe(val gameGroupingId: Int? = null, val tournamentId: Int? = null) : GameGroupingAction() {
        override val action: Action = Action.Subscribe
    }

    /**
     * Unsubscribes the socket from updates to the list of game groupings, previously subscribed by [Subscribe]. A
     * successful response to this would be [GameGroupingReaction.Unsubscribed]
     */
    @Serializable
    data class Unsubscribe(val gameGroupingId: Int? = null, val tournamentId: Int? = null) : GameGroupingAction() {
        override val action: Action = Action.Unsubscribe
    }
}
