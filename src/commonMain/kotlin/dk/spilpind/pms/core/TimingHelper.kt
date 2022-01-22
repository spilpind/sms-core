package dk.spilpind.pms.core

import kotlinx.datetime.*

/**
 * Various utility functionality useful when dealing with timing in a game
 */
object TimingHelper {

    private val currentInstant: Instant
        get() = Clock.System.now()

    private val timeZone = TimeZone.currentSystemDefault()

    /**
     * Returns a string representing the current date and time. This can be used e.g. as database values
     */
    val currentDateTimeString: String
        get() = currentInstant.toLocalDateTime(timeZone).toString()

    /**
     * Parses [isoDateString] to a date time object. This can e.g. parse values from [currentDateTimeString]
     */
    fun parse(isoDateString: String): LocalDateTime {
        return if (isoDateString.length >= 24) {
            // Javascript Date.toISOString() for instance returns this
            isoDateString.toInstant().toLocalDateTime(timeZone)
        } else {
            isoDateString.toLocalDateTime()
        }
    }

    /**
     * Calculates the difference between the provided date time and now, i.e. how long time since the date time occurred
     */
    fun LocalDateTime.secondsUntilNow(): Int {
        val instantInThePast = toInstant(timeZone)
        return instantInThePast.until(currentInstant, DateTimeUnit.SECOND, timeZone).toInt()
    }

}
