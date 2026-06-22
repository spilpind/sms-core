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

    /**
     * Game rules applied to every game in the tournament that doesn't itself specify a [Game.gameRulesId] nor inherit
     * rules from its [GameGrouping]. When null, [GameRules.Standard] is used as a last resort
     */
    val gameRulesId: GameRules.Custom.Id?

    /**
     * Known tags that can be applied to a tournament via [tags]. Use [ModelHelper.hasTag] to check for presence
     */
    enum class Tag(val identifier: String) {
        StickLeague("stick-league"),
        StickLeagueCurrent("stick-league-current"),
        StandingsGameCount("standings-game-count"),
        StandingsGameTime("standings-game-time"),
        ChampionshipDanish("championship-danish"),
    }

    /**
     * Defines how a tournament's standings are structured. The two types are mutually exclusive. Can be resolved from
     * tags via [ModelHelper.standingsType]
     */
    enum class StandingsType(val tag: Tag) {
        GameCount(Tag.StandingsGameCount),
        GameTime(Tag.StandingsGameTime),
    }

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
        override val gameRulesId: GameRules.Custom.Id?,
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
        val rules: GameRules.Custom?,
    ) : Tournament {
        override val gameRulesId = rules?.gameRulesId
    }
}
