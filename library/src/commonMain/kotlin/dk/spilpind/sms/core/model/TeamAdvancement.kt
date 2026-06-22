package dk.spilpind.sms.core.model

import dk.spilpind.sms.core.model.Game as GameModel
import dk.spilpind.sms.core.model.GameGrouping as GameGroupingModel
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
     * Reference to either end of a [TeamAdvancement], i.e. what a team advances from ([source]) or to ([destination])
     */
    sealed interface Reference {

        /**
         * References a specific game. For a game, [TeamAdvancement.sourcePlacement] is 1 (winner) or 2 (loser)
         */
        data class Game(val gameId: GameModel.Id) : Reference

        /**
         * References a specific game grouping. For a grouping, [TeamAdvancement.sourcePlacement] is the rank within the
         * grouping (e.g. 1 is first place, 3 is third place)
         */
        data class GameGrouping(val gameGroupingId: GameGroupingModel.Id) : Reference
    }
}
