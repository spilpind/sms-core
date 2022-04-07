package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.UserRole]
 */
@Serializable
sealed class UserRoleAction : ContextAction() {
    override val context: Context = Context.UserRole

    /**
     * Adds a new user role. A successful response to this would be [UserRoleReaction.Added]
     */
    @Serializable
    data class Add(
        val userId: Int,
        val roleContext: String,
        val contextId: Int,
        val role: String,
        val isPublic: Boolean
    ) : UserRoleAction() {
        override val action: Action = Action.Add
        override val minimumAccessLevel: Int? = null
    }
}
