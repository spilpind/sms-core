package dk.spilpind.pms.api.data

import dk.spilpind.pms.api.action.UserAction
import dk.spilpind.pms.api.action.UserReaction
import kotlinx.serialization.Serializable

/**
 * Data classes used when performing user actions and reactions
 */
object UserData {

    /**
     * Data for the [UserAction.Add] request
     */
    @Serializable
    data class Add(val name: String, val username: String, val email: String, val accessLevel: Int)

    /**
     * Data for the [UserAction.Remove] request
     */
    @Serializable
    data class Remove(val userId: Int)

    /**
     * Data for the [UserReaction.Removed] response
     */
    @Serializable
    data class Removed(val userId: Int)

    /**
     * Data for the [UserAction.Fetch] request
     */
    @Serializable
    data class Fetch(val userId: Int? = null, val email: String? = null)

    /**
     * Data for the [UserReaction.Fetched] response
     */
    @Serializable
    data class Fetched(val userId: Int, val name: String, val username: String, val email: String, val accessLevel: Int)
}
