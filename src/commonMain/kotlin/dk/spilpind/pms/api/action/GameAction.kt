package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.Game]
 */
sealed class GameAction : ContextAction {
    override val context: Context = Context.Game

    /**
     * Adds a new game. [teamAId] and [teamBId] cannot represent the same team. A successful response to this would be
     * [GameReaction.Added]
     */
    @Serializable
    data class Add(
        val tournamentId: Int,
        val teamAId: Int?,
        val teamBId: Int?,
        val description: String,
        val focused: Boolean
    ) : GameAction() {
        override val action: Action = Action.Add
        override val minimumAccessLevel: Int = 2
    }

    /**
     * Removes the game with id [gameId]. The game must not be started. A successful response to this would be
     * [GameReaction.Removed]
     */
    @Serializable
    data class Remove(val gameId: Int) : GameAction() {
        override val action: Action = Action.Remove
        override val minimumAccessLevel: Int = 2
    }

    /**
     * Fetches the game with id [gameId] or all games if it's null. A successful response to this would be
     * [GameReaction.Fetched]
     */
    @Serializable
    data class Fetch(val gameId: Int? = null) : GameAction() {
        override val action: Action = Action.Fetch
        override val minimumAccessLevel: Int? = null
    }
}
