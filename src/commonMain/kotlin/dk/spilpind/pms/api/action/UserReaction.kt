package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.User]. Note that there isn't an add action as it's
 * done via [AuthenticationAction.Add]
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
     * Response to changes in one or more of the users
     */
    @Serializable
    data class Updated(
        val allItems: Boolean,
        val users: List<User>
    ) : UserReaction() {
        override val reaction: Reaction = Reaction.Updated
    }

    /**
     * Response to [UserAction.Subscribe]
     */
    @Serializable
    class Subscribed : UserReaction() {
        override val reaction: Reaction = Reaction.Subscribed
    }

    /**
     * Response to [UserAction.Unsubscribe]
     */
    @Serializable
    class Unsubscribed : UserReaction() {
        override val reaction: Reaction = Reaction.Unsubscribed
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
