package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.core.model.Context
import  dk.spilpind.sms.core.model.PendingRequest
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.Game]
 */
@Serializable
sealed class GameAction : ContextAction() {
    override val context: Context = Context.Game

    /**
     * Adds a new game. [teamAId] and [teamBId] cannot represent the same team and has to be associated with the
     * tournament identified by [tournamentId]. A successful response to this would be [GameReaction.Added]
     */
    @Serializable
    data class Add(
        val tournamentId: Int,
        val teamAId: Int?,
        val teamBId: Int?,
        val description: String,
        val addTeamJoinInvite: Boolean = false
    ) : GameAction() {
        override val action: Action = Action.Add
    }

    /**
     * Removes the game with id [gameId]. The game must not be started. A successful response to this would be
     * [GameReaction.Removed]
     */
    @Serializable
    data class Remove(val gameId: Int) : GameAction() {
        override val action: Action = Action.Remove
    }

    /**
     * Updates the game with id [gameId] with the values provided. It is important to consider all values even though
     * you're not allowed to change them, as for instance a value set to empty or null will be updated to that value
     */
    @Serializable
    data class Update(
        val gameId: Int,
        val teamAId: Int?,
        val teamBId: Int?,
        val description: String,
    ) : GameAction() {
        override val action: Action = Action.Update
    }

    /**
     * Accepts a pending request related to a game (see [PendingRequest.Type.Game]), where [request] has to match value
     * of [PendingRequest.Type.Game.request]. If the type of pending request requires it, [teamId] has to be non-null. A
     * successful response to this would be [GameReaction.Accepted]
     */
    @Serializable
    @Deprecated("Will be remove in version 2. Use AcceptRequest")
    data class Accept(val request: String, val code: String, val teamId: Int? = null) : GameAction() {
        @Suppress("DEPRECATION")
        override val action: Action = Action.Accept
    }

    /**
     * Subscribes to all games (or a single game) associated with the given parameters available for the current user.
     * The subscription will be kept alive until the session is destroyed or [Unsubscribe] is called. Changes to the
     * list will be sent via relevant [GameReaction]s. A successful response to this would be [GameReaction.Subscribed]
     * followed by [GameReaction.Updated]
     */
    @Serializable
    data class Subscribe(val gameId: Int? = null, val tournamentId: Int? = null) : GameAction() {
        override val action: Action = Action.Subscribe
    }

    /**
     * Unsubscribes the socket from updates to the list of games, previously subscribed by [Subscribe]. A successful
     * response to this would be [GameReaction.Unsubscribed]
     */
    @Serializable
    data class Unsubscribe(val gameId: Int? = null, val tournamentId: Int? = null) : GameAction() {
        override val action: Action = Action.Unsubscribe
    }

    /**
     * Creates a pending request related to a game (see [PendingRequest.Type.Game]) identified by [gameId]. [request]
     * has to match value of [PendingRequest.Type.Game.request]. If the request already exists, it will be overwritten
     * (a new code will be generated and its expiration time will be reset). A successful response to this would be
     * [GameReaction.RequestCreated]
     */
    @Serializable
    data class CreateRequest(val request: String, val gameId: Int) : GameAction() {
        override val action: Action = Action.CreateRequest
    }

    /**
     * Accepts a pending request related to a game (see [PendingRequest.Type.Game]), where [request] has to match value
     * of [PendingRequest.Type.Game.request]. If the type of pending request requires it, [teamId] has to be non-null. A
     * successful response to this would be [GameReaction.RequestAccepted]
     */
    @Serializable
    data class AcceptRequest(val request: String, val code: String, val teamId: Int? = null) : GameAction() {
        override val action: Action = Action.AcceptRequest
    }
}
