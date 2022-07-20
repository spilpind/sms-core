package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.core.model.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.Authentication]
 */
@Serializable
sealed class AuthenticationAction : ContextAction() {
    override val context: Context = Context.Authentication

    /**
     * Request to get information about how to authenticate, where [redirectUrl] is the URL the user will be redirected
     * to after authentication. A successful response to this would be [AuthenticationReaction.Informed]
     */
    @Serializable
    data class Inform(val redirectUrl: String) : AuthenticationAction() {
        override val action: Action = Action.Inform
    }

    /**
     * Request to authenticate the user (i.e. add it to the current session). It is required to pass in either
     * [refreshToken] (gained from [AuthenticationReaction.Added.refreshToken]), [googleToken], [googleCode] or
     * [appleCode]. [GoogleCode.redirectUrl] should be the same as for [Inform]. If a valid token cannot be associated
     * with a user, a new user will be created. A successful response to this would be [AuthenticationReaction.Added]
     */
    @Serializable
    data class Add(
        val refreshToken: String? = null,
        val googleToken: String? = null,
        val googleCode: GoogleCode? = null,
        val appleCode: String? = null
    ) : AuthenticationAction() {
        override val action: Action = Action.Add

        @Serializable
        data class GoogleCode(val code: String, val redirectUrl: String)
    }

    /**
     * Request to log out the user (i.e. remove it from the current connection). A successful response to this would be
     * [AuthenticationReaction.Removed]
     */
    @Serializable
    class Remove : AuthenticationAction() {
        override val action: Action = Action.Remove
    }
}
