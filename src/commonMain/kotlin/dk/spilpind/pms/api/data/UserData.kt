package dk.spilpind.pms.api.data

import kotlinx.serialization.Serializable

object UserData {

    @Serializable
    data class Add(val name: String, val username: String, val email: String, val accessLevel: Int)

    @Serializable
    data class Remove(val userId: Int)

    @Serializable
    data class Removed(val userId: Int)

    @Serializable
    data class Fetch(val userId: Int? = null, val email: String? = null)

    @Serializable
    data class Fetched(val userId: Int, val name: String, val username: String, val email: String, val accessLevel: Int)
}
