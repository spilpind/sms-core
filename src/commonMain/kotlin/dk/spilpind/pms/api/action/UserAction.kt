package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.User]
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
     * Fetches the user with id [userId] or all users if it's null. A successful response to this would be
     * [UserReaction.Fetched]
     */
    @Serializable
    data class Fetch(val userId: Int? = null) : UserAction() {
        override val action: Action = Action.Fetch
        override val minimumAccessLevel: Int? = null
    }
}
