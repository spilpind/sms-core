package dk.spilpind.sms.core.model

import kotlin.jvm.JvmInline

/**
 * Represents data about a team
 */
sealed interface Team {
    val teamId: Id
    val name: String
    val shortName: String
    val tournamentId: Tournament.Id

    /**
     * Id of a team. Can be used to reference a team without having to care about the remaining team data
     */
    @JvmInline
    value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

        override fun compareTo(other: Id): Int =
            identifier.compareTo(other.identifier)

    }

    /**
     * Represents the raw team
     */
    data class Raw(
        override val teamId: Id,
        override val name: String,
        override val shortName: String,
        override val tournamentId: Tournament.Id
    ) : Team

    /**
     * Like [Raw] with an actual representation of the tournament
     */
    data class Detailed(
        override val teamId: Id,
        override val name: String,
        override val shortName: String,
        val tournament: Tournament
    ) : Team {
        override val tournamentId: Tournament.Id = tournament.tournamentId
    }
}
