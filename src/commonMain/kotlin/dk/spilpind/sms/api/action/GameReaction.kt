package dk.spilpind.sms.api.action

import dk.spilpind.sms.core.model.Context
import dk.spilpind.sms.api.common.Reaction
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.Game]
 */
@Serializable
sealed class GameReaction : ContextReaction() {
    override val context: Context = Context.Game

    /**
     * Response to [GameAction.Add] and updates via subscriptions
     */
    @Serializable
    data class Added(val game: Game) : GameReaction() {
        override val reaction: Reaction = Reaction.Added
    }

    /**
     * Response to [GameAction.Remove] and updates via subscriptions
     */
    @Serializable
    data class Removed(val gameId: Int) : GameReaction() {
        override val reaction: Reaction = Reaction.Removed
    }

    /**
     * Response to changes in one or more of the games. If [allGames] is false, only the listed games should be updated
     * - if it's true, the entire list should be replaced
     */
    @Serializable
    data class Updated(
        val allGames: Boolean,
        val games: List<Game>
    ) : GameReaction() {
        override val reaction: Reaction = Reaction.Updated
    }

    /**
     * Response to [GameAction.Accept]. [game] will represent the game which the pending request was related to
     */
    @Serializable
    data class Accepted(val game: Game) : GameReaction() {
        override val reaction: Reaction = Reaction.Accepted
    }

    /**
     * Response to [GameAction.Subscribe]
     */
    @Serializable
    class Subscribed : GameReaction() {
        override val reaction: Reaction = Reaction.Subscribed
    }

    /**
     * Response to [GameAction.Unsubscribe]
     */
    @Serializable
    class Unsubscribed : GameReaction() {
        override val reaction: Reaction = Reaction.Unsubscribed
    }

    /**
     * Represents a single game
     */
    @Serializable
    data class Game(
        val gameId: Int,
        val tournamentId: Int,
        val teamAId: Int?,
        val teamBId: Int?,
        val gameState: String,
        val teamAPoints: Int,
        val teamBPoints: Int,
        val description: String,
        val teamJoinInviteCode: String? = null
    )
}
