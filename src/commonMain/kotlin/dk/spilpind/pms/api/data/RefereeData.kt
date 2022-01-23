package dk.spilpind.pms.api.data

import kotlinx.serialization.Serializable

object RefereeData {

    @Serializable
    data class Add(
        val gameId: Int,
        val typeId: Int,
        val lastEventId: Int?,
        val startingInTeam: Int? = null,
        val points: Int? = null
    )

    @Serializable
    data class Updated(
        val gameId: Int,
        val tournamentName: String,
        val description: String,
        val gameState: String,
        val inTeam: Team?,
        val outTeam: Team?,
        val faults: Int,
        val dead: Int,
        val gameTime: Int,
        val turnTime: Int,
        val liftSucceeded: Boolean,
        val recentEvents: Collection<EventData.Fetched>
    ) {

        @Serializable
        data class Team(val name: String, val shortName: String, val points: Int)
    }

    @Serializable
    data class Remove(val eventId: Int)

    @Serializable
    data class Subscribe(val gameId: Int)

    @Serializable
    data class Unsubscribe(val gameId: Int)
}
