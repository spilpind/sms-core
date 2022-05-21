package dk.spilpind.sms.core.model

/**
 * Represents overall game data
 */
sealed interface Game {
    val gameId: Int
    val tournamentId: Int
    val teamAId: Int?
    val teamBId: Int?
    val description: String
    val isFocused: Boolean
    val joinInviteCode: String?

    /**
     * Represents the raw game
     */
    data class Raw(
        override val gameId: Int,
        override val tournamentId: Int,
        override val teamAId: Int?,
        override val teamBId: Int?,
        override val description: String,
        override val isFocused: Boolean,
        override val joinInviteCode: String?
    ) : Game

    /**
     * Like [Raw] with actual representations of tournament and the teams
     */
    data class Detailed(
        override val gameId: Int,
        val tournament: Tournament,
        val teamA: Team?,
        val teamB: Team?,
        override val description: String,
        override val isFocused: Boolean,
        override val joinInviteCode: String?
    ) : Game {
        override val tournamentId = tournament.tournamentId
        override val teamAId = teamA?.teamId
        override val teamBId = teamB?.teamId
    }
}
