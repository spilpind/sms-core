package dk.spilpind.sms.core.model

import kotlinx.datetime.LocalDate
import kotlin.jvm.JvmInline

/**
 * Represents data about a tournament
 */
sealed interface Tournament {
    val tournamentId: Id
    val name: String
    val isPublic: Boolean
    val isLocked: Boolean
    val tags: List<String>
    val startDate: LocalDate?
    val endDate: LocalDate?
    val gameRulesId: GameRules.Id?

    /**
     * Id of a tournament. Can be used to reference a tournament without having to care about the tournament game data
     */
    @JvmInline
    value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

        override fun compareTo(other: Id): Int =
            identifier.compareTo(other.identifier)

    }

    /**
     * Represents the raw tournament
     */
    data class Raw(
        override val tournamentId: Id,
        override val name: String,
        override val isPublic: Boolean,
        override val isLocked: Boolean,
        override val tags: List<String>,
        override val startDate: LocalDate?,
        override val endDate: LocalDate?,
        override val gameRulesId: GameRules.Id?,
    ) : Tournament

    /**
     * Like [Raw] with the embedded [rules] referenced by the tournament (if any)
     */
    data class Detailed(
        override val tournamentId: Id,
        override val name: String,
        override val isPublic: Boolean,
        override val isLocked: Boolean,
        override val tags: List<String>,
        override val startDate: LocalDate?,
        override val endDate: LocalDate?,
        val rules: GameRules?,
    ) : Tournament {
        override val gameRulesId = rules?.gameRulesId
    }
}
