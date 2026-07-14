package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Reaction
import dk.spilpind.sms.core.model.Context
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * All possible reactions that can be made in relation to [Context.GameRules]
 */
@Serializable
sealed class GameRulesReaction : ContextReaction() {
    override val context: Context = Context.GameRules

    /**
     * Response to [GameRulesAction.Add]
     */
    @Serializable
    data class Added(val gameRules: GameRules) : GameRulesReaction() {
        override val reaction: Reaction = Reaction.Added
    }

    /**
     * Response to [GameRulesAction.Subscribe]
     */
    @Serializable
    class Subscribed : GameRulesReaction() {
        override val reaction: Reaction = Reaction.Subscribed
    }

    /**
     * Response to [GameRulesAction.Unsubscribe]
     */
    @Serializable
    class Unsubscribed : GameRulesReaction() {
        override val reaction: Reaction = Reaction.Unsubscribed
    }

    /**
     * Response to a change in the subscribed game rules. Carries the current state of the entry
     */
    @Serializable
    data class Updated(val gameRules: GameRules) : GameRulesReaction() {
        override val reaction: Reaction = Reaction.Updated
    }

    /**
     * Represents a single set of game rules
     */
    @Serializable
    data class GameRules(
        val gameRulesId: Int,
        val gameTimeThreshold: Duration?,
        val gameTimeExtension: Duration? = null,
        val gamePointThreshold: Int?,
        val turnTimeThreshold: Duration?,
        val turnDeathThreshold: Int,
    )
}
