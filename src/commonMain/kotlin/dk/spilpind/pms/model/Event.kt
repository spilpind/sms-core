package dk.spilpind.pms.model

/**
 * Type ids are grouped as follows:
 * - 10+: overall stuff
 * - 20+: match time
 * - 30+: fault
 * - 40+: switch
 */
sealed interface Event {
    val eventId: Int?
    val game: Game
    val team: Team
    val typeId: Int
    val time: Int
    val referee: User

    data class Points(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User,
        val pointId: Int,
        val pointCount: Int,
    ) : Event {
        override val typeId: Int = 11
    }

    data class Dead(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 12
    }

    data class GameStart(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 21
    }

    data class GameEnd(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 22
    }

    data class PauseStart(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 25
    }

    data class PauseEnd(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 26
    }

    data class FaultClick(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 31
    }

    data class FaultBacklift(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 32
    }

    data class FaultRoll(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 33
    }

    data class FaultOut(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 34
    }

    data class FaultCatch(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 35
    }

    data class FaultWicketDirect(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 36
    }

    data class FaultWicketShin(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 37
    }

    data class FaultHitCatch(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 38
    }

    data class SwitchForce(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 41
    }

    data class SwitchTime(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 42
    }

    data class SwitchDead(
        override val eventId: Int?,
        override val game: Game,
        override val team: Team,
        override val time: Int,
        override val referee: User
    ) : Event {
        override val typeId: Int = 43
    }
}