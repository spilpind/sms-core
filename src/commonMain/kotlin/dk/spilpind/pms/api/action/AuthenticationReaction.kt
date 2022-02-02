package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.Authentication]
 */
sealed interface AuthenticationReaction : ContextReaction {

    /**
     * Response to [AuthenticationAction.Inform]. Send the user to [googleLoginUrl] in order to authenticate
     */
    @Serializable
    data class Informed(val googleLoginUrl: String) : AuthenticationReaction {
        override val reaction: Reaction = Reaction.Informed
    }

    /**
     * Response to [AuthenticationAction.Fetch]. [jsonWebToken] should be used for any requests requiring authentication
     */
    @Serializable
    data class Fetched(val jsonWebToken: String) : AuthenticationReaction {
        override val reaction: Reaction = Reaction.Fetched
    }
}


