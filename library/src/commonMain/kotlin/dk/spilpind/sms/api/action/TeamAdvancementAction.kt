package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.core.model.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.TeamAdvancement].
 *
 * Both ends of an advancement are referenced via a type + id pair: [Add.sourceType]/[Add.sourceId] and
 * [Add.destinationType]/[Add.destinationId], where the type is either `"game"` or `"gameGrouping"`
 */
@Serializable
sealed class TeamAdvancementAction : ContextAction() {
    override val context: Context = Context.TeamAdvancement

    /**
     * Adds a new team advancement scoped to the tournament identified by [tournamentId]. The team with
     * [sourcePlacement] in the source (identified by [sourceType] and [sourceId]) is meant to be transferred to the
     * destination (identified by [destinationType] and [destinationId]). A successful response to this would be
     * [TeamAdvancementReaction.Added]
     */
    @Serializable
    data class Add(
        val tournamentId: Int,
        val sourceType: String,
        val sourceId: Int,
        val sourcePlacement: Int,
        val destinationType: String,
        val destinationId: Int,
    ) : TeamAdvancementAction() {
        override val action: Action = Action.Add
    }

    /**
     * Removes the team advancement with id [teamAdvancementId]. A successful response to this would be
     * [TeamAdvancementReaction.Removed]
     */
    @Serializable
    data class Remove(val teamAdvancementId: Int) : TeamAdvancementAction() {
        override val action: Action = Action.Remove
    }

    /**
     * Updates the team advancement with id [teamAdvancementId] with the values provided. It is important to consider all
     * values even though you're not allowed to change them, as for instance a value set to empty or null will be
     * updated to that value
     */
    @Serializable
    data class Update(
        val teamAdvancementId: Int,
        val sourceType: String,
        val sourceId: Int,
        val sourcePlacement: Int,
        val destinationType: String,
        val destinationId: Int,
    ) : TeamAdvancementAction() {
        override val action: Action = Action.Update
    }

    /**
     * Subscribes to team advancements associated with the given parameters: all advancements of a [tournamentId], or
     * those referencing a specific [gameId] or [gameGroupingId]. The subscription will be kept alive until the session
     * is destroyed or [Unsubscribe] is called. Changes to the list will be sent via relevant
     * [TeamAdvancementReaction]s. A successful response to this would be [TeamAdvancementReaction.Subscribed] followed
     * by [TeamAdvancementReaction.Updated]
     */
    @Serializable
    data class Subscribe(
        val tournamentId: Int? = null,
        val gameId: Int? = null,
        val gameGroupingId: Int? = null,
    ) : TeamAdvancementAction() {
        override val action: Action = Action.Subscribe
    }

    /**
     * Unsubscribes the socket from updates to the list of team advancements, previously subscribed by [Subscribe]. A
     * successful response to this would be [TeamAdvancementReaction.Unsubscribed]
     */
    @Serializable
    data class Unsubscribe(
        val tournamentId: Int? = null,
        val gameId: Int? = null,
        val gameGroupingId: Int? = null,
    ) : TeamAdvancementAction() {
        override val action: Action = Action.Unsubscribe
    }
}
