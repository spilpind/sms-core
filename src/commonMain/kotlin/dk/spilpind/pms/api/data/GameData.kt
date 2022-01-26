package dk.spilpind.pms.api.data

import dk.spilpind.pms.api.action.GameAction
import dk.spilpind.pms.api.action.GameReaction
import kotlinx.serialization.Serializable

/**
 * Data classes used when performing game actions and reactions
 */
object GameData {

    /**
     * Data for the [GameAction.Add] request
     */
    @Serializable
    data class Add(
        val tournamentId: Int,
        val teamAId: Int?,
        val teamBId: Int?,
        val description: String,
        val focused: Boolean
    )

    /**
     * Data for the [GameAction.Remove] request
     */
    @Serializable
    data class Remove(val gameId: Int)

    /**
     * Data for the [GameReaction.Removed] response
     */
    @Serializable
    data class Removed(val gameId: Int)

    /**
     * Data for the [GameAction.Fetch] request
     */
    @Serializable
    data class Fetch(val gameId: Int? = null)

    /**
     * Data for the [GameReaction.Fetched] response
     */
    @Serializable
    data class Fetched(val gameId: Int, val description: String, val focused: Boolean)
}
