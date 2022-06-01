package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.core.model.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.User]. Note that there isn't an add action as it's
 * done via [AuthenticationAction.Add]
 */
@Serializable
sealed class UserAction : ContextAction() {
    override val context: Context = Context.User

    /**
     * Removes the user with id [userId]. The user must not be associated any teams, games or alike. A successful
     * response to this would be [UserReaction.Removed]
     */
    @Serializable
    data class Remove(val userId: Int) : UserAction() {
        override val action: Action = Action.Remove
        override val minimumAccessLevel: Int = 2
    }

    /**
     * Subscribes to all users (or a single user) associated with the given parameters available for the current
     * logged-in user. The subscription will be kept alive until the session is destroyed or [Unsubscribe] is called.
     * Changes to the list will be sent via relevant [UserReaction]s. A successful response to this would be
     * [UserReaction.Subscribed] followed by [UserReaction.Updated]
     */
    @Serializable
    data class Subscribe(val userId: Int?) : UserAction() {
        override val action: Action = Action.Subscribe
        override val minimumAccessLevel: Int? = null
    }

    /**
     * Unsubscribes the socket from updates to the list of users, previously subscribed by [Subscribe]. A successful
     * response to this would be [UserReaction.Unsubscribed]
     */
    @Serializable
    data class Unsubscribe(val userId: Int?) : UserAction() {
        override val action: Action = Action.Unsubscribe
        override val minimumAccessLevel: Int? = null
    }
}
