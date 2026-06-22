package dk.spilpind.sms.api.action

import dk.spilpind.sms.core.model.Context
import dk.spilpind.sms.api.common.Reaction
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.TeamAdvancement]
 */
@Serializable
sealed class TeamAdvancementReaction : ContextReaction() {
    override val context: Context = Context.TeamAdvancement

    /**
     * Response to [TeamAdvancementAction.Add] and updates via subscriptions
     */
    @Serializable
    data class Added(val teamAdvancement: TeamAdvancement) : TeamAdvancementReaction() {
        override val reaction: Reaction = Reaction.Added
    }

    /**
     * Response to [TeamAdvancementAction.Remove] and updates via subscriptions
     */
    @Serializable
    data class Removed(val teamAdvancementId: Int) : TeamAdvancementReaction() {
        override val reaction: Reaction = Reaction.Removed
    }

    /**
     * Response to changes in one or more of the team advancements. If [allTeamAdvancements] is false, only the listed
     * team advancements should be updated - if it's true, the entire list should be replaced
     */
    @Serializable
    data class Updated(
        val allTeamAdvancements: Boolean,
        val teamAdvancements: List<TeamAdvancement>
    ) : TeamAdvancementReaction() {
        override val reaction: Reaction = Reaction.Updated
    }

    /**
     * Response to [TeamAdvancementAction.Subscribe]
     */
    @Serializable
    class Subscribed : TeamAdvancementReaction() {
        override val reaction: Reaction = Reaction.Subscribed
    }

    /**
     * Response to [TeamAdvancementAction.Unsubscribe]
     */
    @Serializable
    class Unsubscribed : TeamAdvancementReaction() {
        override val reaction: Reaction = Reaction.Unsubscribed
    }

    /**
     * Represents a single team advancement. Both ends are referenced via a type + id pair, where the type
     * ([sourceType]/[destinationType]) is either `"game"` or `"gameGrouping"`
     */
    @Serializable
    data class TeamAdvancement(
        val teamAdvancementId: Int,
        val tournamentId: Int,
        val sourceType: String,
        val sourceId: Int,
        val sourcePlacement: Int,
        val destinationType: String,
        val destinationId: Int,
    )
}
