package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.Game]
 */
@Serializable
sealed class GameAction : ContextAction() {
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
        val isFocused: Boolean,
        val addJoinInvite: Boolean = false
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

    /**
     * Accepts an invite related to a game. A successful response to this would be [GameReaction.Accepted]
     */
    @Serializable
    data class Accept(val inviteAction: String, val code: String, val teamId: Int? = null) : GameAction() {
        override val action: Action = Action.Accept
        override val minimumAccessLevel: Int? = null
    }

    /**
     * Subscribes the socket to all games (or a single game) available for the current user and associated with the
     * given parameters. The subscription will be kept alive until the socket disconnects or [Unsubscribe] is called.
     * Changes to the list will be sent to the socket, via relevant [GameReaction]s. A successful response to this would
     * be [GameReaction.Subscribed] followed by [GameReaction.Updated]
     */
    @Serializable
    data class Subscribe(val gameId: Int? = null, val tournamentId: Int? = null) : GameAction() {
        override val action: Action = Action.Subscribe
        override val minimumAccessLevel: Int? = null
    }

    /**
     * Unsubscribes the socket from updates to the list of games, previously subscribed by [Subscribe]. A successful
     * response to this would be [GameReaction.Unsubscribed]
     */
    @Serializable
    data class Unsubscribe(val gameId: Int? = null, val tournamentId: Int? = null) : GameAction() {
        override val action: Action = Action.Unsubscribe
        override val minimumAccessLevel: Int? = null
    }
}
