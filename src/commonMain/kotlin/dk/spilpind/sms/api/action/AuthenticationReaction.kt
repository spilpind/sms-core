package dk.spilpind.sms.api.action

import dk.spilpind.sms.core.model.Context
import dk.spilpind.sms.api.common.Reaction
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.Authentication]
 */
@Serializable
sealed class AuthenticationReaction : ContextReaction() {
    override val context: Context = Context.Authentication

    /**
     * Response to [AuthenticationAction.Inform]. Send the user to [googleLoginUrl] in order to authenticate
     */
    @Serializable
    data class Informed(val googleLoginUrl: String) : AuthenticationReaction() {
        override val reaction: Reaction = Reaction.Informed
    }

    /**
     * Response to [AuthenticationAction.Add]. If included, the [refreshToken] can be used then adding an authentication
     * later on (if not expired or alike)
     */
    @Serializable
    data class Added(val userId: Int, val refreshToken: String?) : AuthenticationReaction() {
        override val reaction: Reaction = Reaction.Added
    }

    /**
     * Response to [AuthenticationAction.Remove]
     */
    @Serializable
    class Removed : AuthenticationReaction() {
        override val reaction: Reaction = Reaction.Removed
    }
}


