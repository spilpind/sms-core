package dk.spilpind.pms.api.data

import kotlinx.serialization.Serializable

object GameData {

    @Serializable
    data class Add(
        val tournamentId: Int,
        val teamAId: Int?,
        val teamBId: Int?,
        val description: String,
        val focused: Boolean
    )

    @Serializable
    data class Remove(val gameId: Int)

    @Serializable
    data class Removed(val gameId: Int)

    @Serializable
    data class Fetch(val gameId: Int? = null)

    @Serializable
    data class Fetched(val gameId: Int, val description: String, val focused: Boolean)
}
