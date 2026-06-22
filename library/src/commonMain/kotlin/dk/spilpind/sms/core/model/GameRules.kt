package dk.spilpind.sms.core.model

import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * Represents a set of game rules. These can either be [Standard] (from the official rule set) or be [Custom] when
 * overridden/adjusted. Where a game's rules originate is captured separately by [Effective.Source]. Nulls for specific
 * rules indicate that the corresponding restriction is disabled - they are not placeholders that should be resolved
 * from the standard rule set
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
     * Overriden/adjusted rules
     */
    data class Custom(
        val gameRulesId: Id,
        override val gameTimeThreshold: Duration?,
        override val gamePointThreshold: Int?,
        override val turnTimeThreshold: Duration?,
        override val turnDeathThreshold: Int,
    ) : GameRules {

        /**
         * Id of a game rules entry. Can be used to reference an entry without having to care about the remaining data
         */
        @JvmInline
        value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

            override fun compareTo(other: Id): Int =
                identifier.compareTo(other.identifier)

        }

        // This isn't possible to override yet, so we just default to the standard
        override val liftFaultThreshold: Int = Standard.liftFaultThreshold
    }

    /**
     * The rules that effectively apply to a game, along with where they originate. [rules] is [Standard] when [source]
     * is [Source.Standard] and a [Custom] otherwise
     */
    data class Effective(
        val source: Source,
        val rules: GameRules,
    ) {
        enum class Source {
            Standard, // The official/standard rule set applies (rules == GameRules.Standard)
            Tournament, // Inherited from the game's tournament
            GameGrouping, // Inherited from the game's grouping
            Game, // Attached directly to the game
        }
    }
}
