package dk.spilpind.sms.core.model

import kotlin.jvm.JvmInline

/**
 * Represents a named group of games within a tournament, e.g. an actual group ("Group A") or just a label for a phase
 * ("Quarterfinals"). [level] describes how far into the tournament the grouping is, where a lower value is more
 * important - e.g. 1 represents the final
 */
data class GameGrouping(
    val gameGroupingId: Id,
    val tournamentId: Tournament.Id,
    val name: String,
    val level: Int,
) {

    /**
     * Id of a game grouping. Can be used to reference a game grouping without having to care about the remaining data
     */
    @JvmInline
    value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

        override fun compareTo(other: Id): Int =
            identifier.compareTo(other.identifier)

    }
}
