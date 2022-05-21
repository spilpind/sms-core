package dk.spilpind.pms.core.model

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

    data class Raw(
        override val gameId: Int,
        override val tournamentId: Int,
        override val teamAId: Int?,
        override val teamBId: Int?,
        override val description: String,
        override val isFocused: Boolean,
        override val joinInviteCode: String?
    ) : Game

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
