package dk.spilpind.sms.core.model

import kotlin.jvm.JvmInline

/**
 * Represents overall game data. If provided, [teamJoinInviteCode] can be used for accepting a pending request of type
 * [PendingRequest.Type.Game.TeamJoinInvite]
 */
sealed interface Game {
    val gameId: Id
    val tournamentId: Tournament.Id
    val teamAId: Team.Id?
    val teamBId: Team.Id?
    val gameState: String
    val teamAPoints: Int
    val teamBPoints: Int
    val description: String
    val teamJoinInviteCode: String?
    val refereeInviteCode: String?

    /**
     * Id of a game. Can be used to reference a game without having to care about the remaining game data
     */
    @JvmInline
    value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

        override fun compareTo(other: Id): Int =
            identifier.compareTo(other.identifier)

    }

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
     * A [Game] that exposes its state as a typed [State] value rather than only the raw [gameState] string
     */
    sealed interface Typed : Game {
        val state: State

        override val gameState: String
            get() = state.identifier
    }

    /**
     * Represents the raw game
     */
    data class Raw(
        override val gameId: Id,
        override val tournamentId: Tournament.Id,
        override val teamAId: Team.Id?,
        override val teamBId: Team.Id?,
        override val gameState: String,
        override val teamAPoints: Int,
        override val teamBPoints: Int,
        override val description: String,
        override val teamJoinInviteCode: String?,
        override val refereeInviteCode: String?
    ) : Game

    /**
     * Like [Raw] with an actual representation of the game state
     */
    data class Simple(
        override val gameId: Id,
        override val tournamentId: Tournament.Id,
        override val teamAId: Team.Id?,
        override val teamBId: Team.Id?,
        override val state: State,
        override val teamAPoints: Int,
        override val teamBPoints: Int,
        override val description: String,
        override val teamJoinInviteCode: String?,
        override val refereeInviteCode: String?
    ) : Typed

    /**
     * Like [Simple] with actual representations of tournament and the teams
     */
    data class Detailed(
        override val gameId: Id,
        val tournament: Tournament,
        val teamA: Team?,
        val teamB: Team?,
        override val state: State,
        override val teamAPoints: Int,
        override val teamBPoints: Int,
        override val description: String,
        override val teamJoinInviteCode: String?,
        override val refereeInviteCode: String?
    ) : Typed {
        override val tournamentId = tournament.tournamentId
        override val teamAId = teamA?.teamId
        override val teamBId = teamB?.teamId
    }
}
