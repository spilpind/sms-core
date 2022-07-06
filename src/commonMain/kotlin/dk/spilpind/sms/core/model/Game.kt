package dk.spilpind.sms.core.model

/**
 * Represents overall game data. If provided, [teamJoinInviteCode] can be used for accepting a pending request of type
 * [PendingRequest.Type.Game.TeamJoinInvite]
 */
sealed interface Game {
    val gameId: Int
    val tournamentId: Int
    val teamAId: Int?
    val teamBId: Int?
    val gameState: String
    val description: String
    val teamJoinInviteCode: String?

    /**
     * Represents available states of a game. [NOT_STARTED] is expected if the game hasn't started and [FINISHED] is to
     * be used in case the game is done. [STARTED] is when the game is active and [PAUSED] represents when the game (and
     * time) has temporarily been stopped. This means [NOT_STARTED], [STARTED] and [FINISHED] are always expected at
     * some point during a game (and in that order) and [PAUSED] should only exist between two [STARTED] states
     */
    enum class State(val identifier: String) {
        NOT_STARTED("notStarted"),
        STARTED("started"),
        PAUSED("paused"),
        FINISHED("finished")
    }

    /**
     * Represents the raw game
     */
    data class Raw(
        override val gameId: Int,
        override val tournamentId: Int,
        override val teamAId: Int?,
        override val teamBId: Int?,
        override val gameState: String,
        override val description: String,
        override val teamJoinInviteCode: String?
    ) : Game

    /**
     * Like [Raw] with an actual representation of the game state
     */
    data class Simple(
        override val gameId: Int,
        override val tournamentId: Int,
        override val teamAId: Int?,
        override val teamBId: Int?,
        val state: State,
        override val description: String,
        override val teamJoinInviteCode: String?
    ) : Game {
        override val gameState: String = state.identifier
    }

    /**
     * Like [Simple] with actual representations of tournament and the teams
     */
    data class Detailed(
        override val gameId: Int,
        val tournament: Tournament,
        val teamA: Team?,
        val teamB: Team?,
        val state: State,
        override val description: String,
        override val teamJoinInviteCode: String?
    ) : Game {
        override val tournamentId = tournament.tournamentId
        override val teamAId = teamA?.teamId
        override val teamBId = teamB?.teamId
        override val gameState: String = state.identifier
    }
}
