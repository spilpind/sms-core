package dk.spilpind.pms.api.data

import kotlinx.serialization.Serializable

object AuthenticationData {

    @Serializable
    data class Inform(val redirectUrl: String)

    @Serializable
    data class Informed(val googleLoginUrl: String)

    @Serializable
    data class Fetch(val googleToken: String? = null, val googleCode: String? = null, val redirectUrl: String? = null)

    @Serializable
    data class Fetched(val jsonWebToken: String)
}
