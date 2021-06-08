package dk.spilpind.pms.core

import kotlinx.datetime.*

object TimingHelper {

    private val currentInstant: Instant
        get() = Clock.System.now()

    private val timeZone = TimeZone.currentSystemDefault()

    val currentDateTimeString: String
        get() = currentInstant.toLocalDateTime(timeZone).toString()

    fun parse(isoDateString: String): LocalDateTime {
        return if (isoDateString.length >= 24) {
            // Javascript Date.toISOString() for instance returns this
            isoDateString.toInstant().toLocalDateTime(timeZone)
        } else {
            isoDateString.toLocalDateTime()
        }
    }

    fun LocalDateTime.secondsUntilNow(): Int {
        val instantInThePast = toInstant(timeZone)
        return instantInThePast.until(currentInstant, DateTimeUnit.SECOND, timeZone).toInt()
    }

}
