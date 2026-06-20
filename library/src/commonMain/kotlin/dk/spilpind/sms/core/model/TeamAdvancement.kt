package dk.spilpind.sms.core.model

import kotlin.jvm.JvmInline

/**
 * Represents how a team is meant to be transferred within a tournament: the team with [sourcePlacement] in [source]
 * should advance to [destination]. Nothing happens automatically based on this - it only records the intended structure
 * so it can be displayed (e.g. "winner of the quarterfinal advances to the semifinal")
 */
data class TeamAdvancement(
    val teamAdvancementId: Id,
    val tournamentId: Tournament.Id,
    val source: Reference,
    val sourcePlacement: Int,
    val destination: Reference,
) {

    /**
     * Id of a team advancement. Can be used to reference a team advancement without having to care about the remaining
     * data
     */
    @JvmInline
    value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

        override fun compareTo(other: Id): Int =
            identifier.compareTo(other.identifier)

    }

    /**
     * Reference to either end of a [TeamAdvancement], i.e. what a team is transferred from ([source]) or to
     * ([destination])
     */
    sealed interface Reference {

        /**
         * References a specific game. For a game, [TeamAdvancement.sourcePlacement] is typically 1 (winner) or 2 (loser)
         */
        data class Game(val gameId: dk.spilpind.sms.core.model.Game.Id) : Reference

        /**
         * References a specific game grouping. For a grouping, [TeamAdvancement.sourcePlacement] is typically the rank
         * within the grouping (1..n)
         */
        data class GameGrouping(val gameGroupingId: dk.spilpind.sms.core.model.GameGrouping.Id) : Reference
    }
}
