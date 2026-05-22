package dk.spilpind.sms.core.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * System defaults applied to games that don't have a [GameRules] attached - either directly or via their tournament.
 * Each threshold here mirrors a field on [GameRules]; a null value means the corresponding restriction is disabled by
 * default
 */
object GameConstants {
    val DEFAULT_GAME_TIME_THRESHOLD: Duration? = 20.minutes
    val DEFAULT_GAME_POINT_THRESHOLD: Int? = null
    val DEFAULT_TURN_TIME_THRESHOLD: Duration? = 5.minutes
    val DEFAULT_TURN_DEATH_THRESHOLD: Int = 2
    val DEFAULT_FAULT_THRESHOLD: Int = 3
}
