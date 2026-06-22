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
     * The club the team belongs to, if any
     */
    val clubId: Club.Id?

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
        override val tournamentId: Tournament.Id,
        override val clubId: Club.Id?
    ) : Team

    /**
     * Like [Raw] with an actual representation of the tournament and club
     */
    data class Detailed(
        override val teamId: Id,
        override val name: String,
        override val shortName: String,
        val tournament: Tournament,
        val club: Club?
    ) : Team {
        override val tournamentId: Tournament.Id = tournament.tournamentId
        override val clubId: Club.Id? = club?.clubId
    }
}
