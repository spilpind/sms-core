package dk.spilpind.pms.api.data

import dk.spilpind.pms.api.action.RefereeAction
import dk.spilpind.pms.api.action.RefereeReaction
import kotlinx.serialization.Serializable

/**
 * Data classes used when performing referee actions and reactions
 */
object RefereeData {

    /**
     * Data for the [RefereeAction.Add] request
     */
    @Serializable
    data class Add(
        val gameId: Int,
        val typeId: Int,
        val lastEventId: Int?,
        val startingInTeam: Int? = null,
        val points: Int? = null
    )

    /**
     * Data for the [RefereeReaction.Updated] response
     */
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

        /**
         * Represents a team in the game
         */
        @Serializable
        data class Team(val name: String, val shortName: String, val points: Int)
    }

    /**
     * Data for the [RefereeAction.Remove] request
     */
    @Serializable
    data class Remove(val eventId: Int)

    /**
     * Data for the [RefereeAction.Subscribe] request
     */
    @Serializable
    data class Subscribe(val gameId: Int)

    /**
     * Data for the [RefereeAction.Unsubscribe] request
     */
    @Serializable
    data class Unsubscribe(val gameId: Int)
}
