package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction
import dk.spilpind.pms.api.data.FetchedData
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.User]
 */
@Serializable
sealed class UserReaction : ContextReaction() {
    override val context: Context = Context.User

    /**
     * Response to [UserAction.Remove]
     */
    @Serializable
    data class Removed(val userId: Int) : UserReaction() {
        override val reaction: Reaction = Reaction.Removed
    }

    /**
     * Response to [UserAction.Fetch]. If the request specified a specific user id, [items] will contain that user as
     * the only item
     */
    @Serializable
    data class Fetched(override val items: List<User>) : UserReaction(), FetchedData<User> {
        override val reaction: Reaction = Reaction.Fetched
    }

    /**
     * Represents a single user
     */
    @Serializable
    data class User(
        val userId: Int,
        val name: String,
        val username: String,
        val email: String,
        val accessLevel: Int
    )
}
