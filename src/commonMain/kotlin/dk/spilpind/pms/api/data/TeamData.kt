package dk.spilpind.pms.api.data

import kotlinx.serialization.Serializable

object TeamData {

    @Serializable
    data class Add(
        val name: String,
        val shortName: String,
        val tournamentId: Int
    )

    @Serializable
    data class Remove(val teamId: Int)

    @Serializable
    data class Removed(val teamId: Int)

    @Serializable
    data class Fetch(val teamId: Int? = null)

    @Serializable
    data class Fetched(val teamId: Int, val name: String, val shortName: String, val tournament: TournamentData.Fetched)
}
