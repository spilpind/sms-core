package dk.spilpind.sms.core.model

import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * Represents a set of game rules. These can either be [Standard] (from the official rule set) or be [Custom] when
 * attached to a [Custom.Source]. Nulls for specific rules indicate that the corresponding restriction is disabled -
 * they are not placeholders that should be resolved from the standard rule set
 */
sealed interface GameRules {
    val gameTimeThreshold: Duration?
    val gamePointThreshold: Int?
    val turnTimeThreshold: Duration?
    val turnDeathThreshold: Int
    val liftFaultThreshold: Int

    /**
     * Standard rules mirroring the official rule set
     */
    data object Standard : GameRules {
        override val gameTimeThreshold: Duration = 20.minutes
        override val gamePointThreshold: Int? = null
        override val turnTimeThreshold: Duration = 5.minutes
        override val turnDeathThreshold: Int = 2
        override val liftFaultThreshold = 3
    }

    /**
     * Overriden/adjusted rules associated with a [source] so it's easier to identify where it comes from
     */
    data class Custom(
        val gameRulesId: Id,
        override val gameTimeThreshold: Duration?,
        override val gamePointThreshold: Int?,
        override val turnTimeThreshold: Duration?,
        override val turnDeathThreshold: Int,
        val source: Source,
    ) : GameRules {

        /**
         * Id of a game rules entry. Can be used to reference an entry without having to care about the remaining data
         */
        @JvmInline
        value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

            override fun compareTo(other: Id): Int =
                identifier.compareTo(other.identifier)

        }

        enum class Source {
            Game,
            Tournament,
        }

        // This isn't possible to override yet, so we just default to the standard
        override val liftFaultThreshold: Int = Standard.liftFaultThreshold
    }
}
