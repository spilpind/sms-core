package dk.spilpind.sms.core

import kotlinx.datetime.*

/**
 * Various utility functionality useful when dealing with time and timing, both in a game and when persisting date times
 */
object TimeHelper {

    private val timeZone = TimeZone.currentSystemDefault()

    /**
     * Returns an object representing the current instant
     */
    val now: Instant
        get() = Clock.System.now()

    /**
     * Returns a representation of the current local date and time
     */
    val currentDateTime: LocalDateTime
        get() = now.toLocalDateTime()

    /**
     * Converts the instant to local date time based on timezone of this device
     */
    fun Instant.toLocalDateTime(): LocalDateTime = toLocalDateTime(timeZone)

    /**
     * Converts the local date time to instant based on timezone of this device
     */
    fun LocalDateTime.toInstant(): Instant = toInstant(timeZone)

    /**
     * Parses [isoDateString] to a date time object. This can e.g. parse a value returned by [LocalDateTime.toString]
     */
    fun parse(isoDateString: String): LocalDateTime {
        return if (isoDateString.length >= 24) {
            // Javascript Date.toISOString() for instance returns this
            Instant.parse(isoDateString).toLocalDateTime(timeZone)
        } else {
            LocalDateTime.parse(isoDateString)
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
