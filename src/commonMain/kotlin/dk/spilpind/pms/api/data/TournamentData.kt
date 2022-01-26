package dk.spilpind.pms.api.data

import dk.spilpind.pms.api.action.TournamentAction
import dk.spilpind.pms.api.action.TournamentReaction
import kotlinx.serialization.Serializable

/**
 * Data classes used when performing tournament actions and reactions
 */
object TournamentData {

    /**
     * Data for the [TournamentAction.Add] request
     */
    @Serializable
    data class Add(val name: String)

    /**
     * Data for the [TournamentAction.Remove] request
     */
    @Serializable
    data class Remove(val tournamentId: Int)

    /**
     * Data for the [TournamentReaction.Removed] response
     */
    @Serializable
    data class Removed(val tournamentId: Int)

    /**
     * Data for the [TournamentAction.Fetch] request
     */
    @Serializable
    data class Fetch(val tournamentId: Int? = null)

    /**
     * Data for the [TournamentReaction.Fetched] response
     */
    @Serializable
    data class Fetched(val tournamentId: Int, val name: String)
}
