package dk.spilpind.sms.core.model

/**
 * Represents data about a team
 */
sealed interface Team {
    val teamId: Int
    val name: String
    val shortName: String
    val tournamentId: Int

    /**
     * Represents the raw team
     */
    data class Raw(
        override val teamId: Int,
        override val name: String,
        override val shortName: String,
        override val tournamentId: Int
    ) : Team

    /**
     * Like [Raw] with an actual representation of the tournament
     */
    data class Detailed(
        override val teamId: Int,
        override val name: String,
        override val shortName: String,
        val tournament: Tournament
    ) : Team {
        override val tournamentId: Int = tournament.tournamentId
    }
}
