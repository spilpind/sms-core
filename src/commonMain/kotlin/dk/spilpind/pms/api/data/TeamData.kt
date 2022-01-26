package dk.spilpind.pms.api.data

import dk.spilpind.pms.api.action.TeamAction
import dk.spilpind.pms.api.action.TeamReaction
import kotlinx.serialization.Serializable

/**
 * Data classes used when performing team actions and reactions
 */
object TeamData {

    /**
     * Data for the [TeamAction.Add] request
     */
    @Serializable
    data class Add(val name: String, val shortName: String, val tournamentId: Int)

    /**
     * Data for the [TeamAction.Remove] request
     */
    @Serializable
    data class Remove(val teamId: Int)

    /**
     * Data for the [TeamReaction.Removed] response
     */
    @Serializable
    data class Removed(val teamId: Int)

    /**
     * Data for the [TeamAction.Fetch] request
     */
    @Serializable
    data class Fetch(val teamId: Int? = null)

    /**
     * Data for the [TeamReaction.Fetched] response
     */
    @Serializable
    data class Fetched(val teamId: Int, val name: String, val shortName: String, val tournament: TournamentData.Fetched)
}
