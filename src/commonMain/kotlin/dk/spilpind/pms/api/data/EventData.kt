package dk.spilpind.pms.api.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

object EventData {

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
