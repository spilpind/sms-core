package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.UserRole]
 */
@Serializable
sealed class UserRoleReaction : ContextReaction() {
    override val context: Context = Context.UserRole

    /**
     * Response to [UserRoleAction.Add]
     */
    @Serializable
    data class Added(val userRole: UserRole) : UserRoleReaction() {
        override val reaction: Reaction = Reaction.Added
    }

    /**
     * Response to changes in one or more of the user roles
     */
    @Serializable
    data class Updated(
        val allItems: Boolean,
        val items: List<UserRole>
    ) : UserRoleReaction() {
        override val reaction: Reaction = Reaction.Updated
    }

    /**
     * Response to [UserRoleAction.Subscribe]
     */
    @Serializable
    class Subscribed : UserRoleReaction() {
        override val reaction: Reaction = Reaction.Subscribed
    }

    /**
     * Response to [UserRoleAction.Unsubscribe]
     */
    @Serializable
    class Unsubscribed : UserRoleReaction() {
        override val reaction: Reaction = Reaction.Unsubscribed
    }

    /**
     * Represents a single user role
     */
    @Serializable
    data class UserRole(
        val userRoleId: Int,
        val userId: Int,
        val roleContext: String,
        val contextId: Int,
        val role: String,
        val isPublic: Boolean
    )
}
