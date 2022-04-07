package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction
import dk.spilpind.pms.api.data.FetchedData
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
    data class Added(
        val userId: Int,
        val roleContext: String,
        val contextId: Int,
        val role: String,
        val isPublic: Boolean
    ) : UserRoleReaction() {
        override val reaction: Reaction = Reaction.Added
    }
}
