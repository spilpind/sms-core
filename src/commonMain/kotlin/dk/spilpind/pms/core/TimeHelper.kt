package dk.spilpind.pms.core

import kotlinx.datetime.*

/**
 * Various utility functionality useful when dealing with time and timing, both in a game and when persisting date times
 */
object TimeHelper {

    private val timeZone = TimeZone.currentSystemDefault()

    /**
     * Returns on object representing the current instant
     */
    val now: Instant
        get() = Clock.System.now()

    /**
     * Returns a string representing the current date and time. This can be used e.g. as database values
     */
    val currentDateTimeString: String
        get() = now.toLocalDateTime().toString()

    /**
     * Converts the instant to local date time based on timezone of this device
     */
    fun Instant.toLocalDateTime(): LocalDateTime = toLocalDateTime(timeZone)

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
        return instantInThePast.until(now, DateTimeUnit.SECOND, timeZone).toInt()
    }

}
