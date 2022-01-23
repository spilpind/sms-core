package dk.spilpind.pms.api.data

import kotlinx.serialization.Serializable

object TournamentData {

    @Serializable
    data class Add(val name: String)

    @Serializable
    data class Remove(val tournamentId: Int)

    @Serializable
    data class Removed(val tournamentId: Int)

    @Serializable
    data class Fetch(val tournamentId: Int? = null)

    @Serializable
    data class Fetched(val tournamentId: Int, val name: String)
}
