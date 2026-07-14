package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.core.model.Context
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * All possible actions that can be made in relation to [Context.GameRules]
 */
@Serializable
sealed class GameRulesAction : ContextAction() {
    override val context: Context = Context.GameRules

    /**
     * Adds a new set of game rules. The created entry is not attached to any game or tournament - that is handled
     * separately. Null values represent disabled restrictions, not placeholders for the system defaults. A successful
     * response to this would be [GameRulesReaction.Added]
     */
    @Serializable
    data class Add(
        val gameTimeThreshold: Duration?,
        val gameTimeExtension: Duration? = null,
        val gamePointThreshold: Int?,
        val turnTimeThreshold: Duration?,
        val turnDeathThreshold: Int,
    ) : GameRulesAction() {
        override val action: Action = Action.Add
    }

    /**
     * Subscribes to changes for the game rules identified by [gameRulesId]. The subscription will be kept alive until
     * the session is destroyed or [Unsubscribe] is called. Changes will be sent via relevant [GameRulesReaction]s. A
     * successful response to this would be [GameRulesReaction.Subscribed] followed by [GameRulesReaction.Updated]
     */
    @Serializable
    data class Subscribe(val gameRulesId: Int) : GameRulesAction() {
        override val action: Action = Action.Subscribe
    }

    /**
     * Unsubscribes the socket from updates to the game rules identified by [gameRulesId], previously subscribed by
     * [Subscribe]. A successful response to this would be [GameRulesReaction.Unsubscribed]
     */
    @Serializable
    data class Unsubscribe(val gameRulesId: Int) : GameRulesAction() {
        override val action: Action = Action.Unsubscribe
    }
}
