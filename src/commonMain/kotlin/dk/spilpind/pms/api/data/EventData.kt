package dk.spilpind.pms.api.data

import dk.spilpind.pms.api.action.RefereeReaction
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * Data classes used when performing event actions and reactions
 */
object EventData {

    /**
     * Data for the event list in the [RefereeReaction.Updated] response
     */
    @Serializable
    data class Fetched(
        val eventId: Int,
        val gameId: Int,
        val teamId: Int?,
        val typeId: Int,
        val time: Int,
        val refereeId: Int,
        val created: LocalDateTime,
        val points: Int? = null
    )
}
