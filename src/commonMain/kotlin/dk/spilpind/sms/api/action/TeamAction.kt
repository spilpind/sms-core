package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.core.model.Context
import dk.spilpind.sms.core.model.PendingRequest
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.Team]
 */
@Serializable
sealed class TeamAction : ContextAction() {
    override val context: Context = Context.Team

    /**
     * Adds a new team. A successful response to this would be [TeamReaction.Added]
     */
    @Serializable
    data class Add(
        val name: String,
        val shortName: String,
        val tournamentId: Int,
        val addAsCaptain: Boolean = false
    ) : TeamAction() {
        override val action: Action = Action.Add
    }

    /**
     * Removes the team with id [teamId]. The team cannot be used for any games or alike. A successful response to this
     * would be [TeamReaction.Removed]
     */
    @Serializable
    data class Remove(val teamId: Int) : TeamAction() {
        override val action: Action = Action.Remove
    }

    /**
     * Subscribes to all teams associated with the tournament identified by [tournamentId] available for the current
     * user. The subscription will be kept alive until the session is destroyed or [Unsubscribe] is called. Changes to
     * the list will be sent via relevant [TeamReaction]s. A successful response to this would be
     * [TeamReaction.Subscribed] followed by [TeamReaction.Updated]
     */
    @Serializable
    data class Subscribe(val tournamentId: Int) : TeamAction() {
        override val action: Action = Action.Subscribe
    }

    /**
     * Unsubscribes the socket from updates to the list of teams, previously subscribed by [Subscribe]. A successful
     * response to this would be [TeamReaction.Unsubscribed]
     */
    @Serializable
    data class Unsubscribe(val tournamentId: Int) : TeamAction() {
        override val action: Action = Action.Unsubscribe
    }

    /**
     * Creates a pending requests related to a team (see [PendingRequest.Type.Team]) identified by [teamId]. [request]
     * has to match value of [PendingRequest.Type.Team.request]. If the request already exists, it will be overwritten
     * (a new code will be generated and its expiration time will be reset). A successful response to this would be
     * [TeamReaction.RequestCreated]
     */
    @Serializable
    data class CreateRequest(val request: String, val teamId: Int) : TeamAction() {
        override val action: Action = Action.CreateRequest
    }

    /**
     * Revokes a pending requests related to a team (see [PendingRequest.Type.Team]) identified by [teamId]. [request]
     * has to match value of [PendingRequest.Type.Team.request]. A successful response to this would be
     * [TeamReaction.RequestRevoked]
     */
    @Serializable
    data class RevokeRequest(val request: String, val teamId: Int) : TeamAction() {
        override val action: Action = Action.RevokeRequest
    }

    /**
     * Accepts a pending requests related to a team (see [PendingRequest.Type.Team]), where [request] has to match value
     * of [PendingRequest.Type.Team.request]. A successful response to this would be [TeamReaction.RequestAccepted]
     */
    @Serializable
    data class AcceptRequest(val request: String, val code: String) : TeamAction() {
        override val action: Action = Action.AcceptRequest
    }
}
