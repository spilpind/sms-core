package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.Authentication]
 */
sealed interface AuthenticationAction : ContextAction {

    /**
     * Request to get information about how to authenticate, where [redirectUrl] should be the URL the user should be
     * redirected to after authentication. A successful response to this would be [AuthenticationReaction.Informed]
     */
    @Serializable
    data class Inform(val redirectUrl: String) : AuthenticationAction {
        override val action: Action = Action.Inform
        override val minimumAccessLevel: Int? = null
    }

    /**
     * Request to authenticate the user. It is required to pass in either [googleToken] or both [googleCode] and
     * [redirectUrl], where [redirectUrl] should be the same as for [Inform]. A successful response to this would be
     * [AuthenticationReaction.Fetched]
     */
    @Serializable
    data class Fetch(
        val googleToken: String? = null,
        val googleCode: String? = null,
        val redirectUrl: String? = null
    ) : AuthenticationAction {
        override val action: Action = Action.Fetch
        override val minimumAccessLevel: Int? = null
    }
}
