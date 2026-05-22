package dk.spilpind.sms.core.model

import kotlin.jvm.JvmInline
import kotlin.time.Duration

/**
 * Represents a set of game rules that overrides the system defaults (see [GameConstants]) when attached to a game or
 * tournament. Nulls indicate that the corresponding restriction is disabled - they are not placeholders that should be
 * resolved from the defaults
 */
data class GameRules(
    val gameRulesId: Id,
    val gameTimeThreshold: Duration?,
    val gamePointThreshold: Int?,
    val turnTimeThreshold: Duration?,
    val turnDeathThreshold: Int,
) {

    /**
     * Id of a game rules entry. Can be used to reference a rules entry without having to care about the remaining data
     */
    @JvmInline
    value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

        override fun compareTo(other: Id): Int =
            identifier.compareTo(other.identifier)

    }
}
