package dk.spilpind.pms.core

import kotlinx.datetime.*

object TimingHelper {

    val currentDatetime: LocalDateTime
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val currentDateTimeString: String
        get() = currentDatetime.toString()

    fun parse(isoDateString: String): LocalDateTime {
        return if (isoDateString.length >= 24) {
            // Javascript Date.toISOString() for instance returns this
            isoDateString.toInstant().toLocalDateTime(TimeZone.currentSystemDefault())
        } else {
            isoDateString.toLocalDateTime()
        }
    }

}
