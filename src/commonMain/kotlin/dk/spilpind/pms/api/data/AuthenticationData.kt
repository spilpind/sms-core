package dk.spilpind.pms.api.data

import dk.spilpind.pms.api.action.AuthenticationAction
import dk.spilpind.pms.api.action.AuthenticationReaction
import kotlinx.serialization.Serializable

/**
 * Data classes used when performing authentication actions and reactions
 */
object AuthenticationData {

    /**
     * Data for the [AuthenticationAction.Inform] request
     */
    @Serializable
    data class Inform(val redirectUrl: String)

    /**
     * Data for the [AuthenticationReaction.Informed] response
     */
    @Serializable
    data class Informed(val googleLoginUrl: String)

    /**
     * Data for the [AuthenticationAction.Fetch] request
     */
    @Serializable
    data class Fetch(val googleToken: String? = null, val googleCode: String? = null, val redirectUrl: String? = null)

    /**
     * Data for the [AuthenticationReaction.Fetched] response
     */
    @Serializable
    data class Fetched(val jsonWebToken: String)
}
