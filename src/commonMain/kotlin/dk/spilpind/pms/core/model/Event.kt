package dk.spilpind.pms.core.model

import kotlinx.datetime.LocalDateTime

/**
 * Representation of all possible events during a game. This does in most cases represent 1:1 what happens in the game
 * and can be used for e.g. calculating time spent in the game and points of the teams. In a few cases more than one
 * event is needed to represent a real life event. This is e.g. the case for events during the game that result in a
 * direct dead as they are all represented as a fault and thus also needs a [Dead] event right after. There's a few
 * reasons for this, one being that it's more generic and could be configured per game-basis if the event should result
 * in a dead or just a fault
 */
sealed interface Event {
    val eventId: Int
    val gameId: Int
    val teamId: Int?
    val typeId: Int
    val time: Int
    val refereeId: Int
    val created: LocalDateTime

    /**
     * All type of events, most important part of this are their unique ids which is useful for e.g. storing the events.
     * The ids are grouped as follows:
     * - 10+: Overall game events stuff
     * - 20+: [Timing] events
     * - 30+: [Fault] events (including events that might result in a direct dead)
     * - 40+: [Switch] events
     */
    enum class Type(val typeId: Int) {
        Points(11),
        Dead(12),
        LiftSuccess(14),
        GameStart(21),
        GameEnd(22),
        PauseStart(25),
        PauseEnd(26),
        FaultClick(31),
        FaultBackLift(32),
        FaultRoll(33),
        FaultOut(34),
        FaultCatch(35),
        FaultWicketDirect(36),
        FaultWicketShin(37),
        FaultHitCatch(38),
        SwitchForce(41),
        SwitchTime(42),
        SwitchDead(43),
    }

    data class Raw(
        override val eventId: Int,
        override val gameId: Int,
        override val teamId: Int?,
        override val typeId: Int,
        override val time: Int,
        override val refereeId: Int,
        override val created: LocalDateTime,
        val points: Int?
    ) : Event

    sealed class Detailed(val type: Type, baseInfo: BaseInfo) : Event {
        override val eventId: Int = baseInfo.eventId
        override val gameId: Int = baseInfo.gameId
        override val teamId: Int? = baseInfo.teamId
        override val typeId = type.typeId
        override val time: Int = baseInfo.time
        override val refereeId: Int = baseInfo.refereeId
        override val created: LocalDateTime = baseInfo.created

        /**
         * Base information that's used for all events
         */
        data class BaseInfo(
            val eventId: Int,
            val gameId: Int,
            val teamId: Int?,
            val time: Int,
            val refereeId: Int,
            val created: LocalDateTime
        )
    }

    /**
     * Represents points given to [teamId] after a successful lift
     */
    data class Points(private val baseInfo: BaseInfo, val points: Int) :
        Detailed(type = Type.Points, baseInfo = baseInfo)

    /**
     * Represents a single dead given to [teamId]
     */
    data class Dead(private val baseInfo: BaseInfo) : Detailed(type = Type.Dead, baseInfo = baseInfo)

    /**
     * Represents a lift without any dead or faults by [teamId]
     */
    data class LiftSuccess(private val baseInfo: BaseInfo) : Detailed(type = Type.LiftSuccess, baseInfo = baseInfo)

    /**
     * Represents a timing event indicated by [timingType]. Only [TimingType.GameStart] is expected to have a [teamId]
     * which should represent the team starting as in team
     */
    data class Timing(private val baseInfo: BaseInfo, val timingType: TimingType) :
        Detailed(type = timingType.type, baseInfo = baseInfo) {

        /**
         * The various timing types.
         */
        enum class TimingType(val type: Type) {
            GameStart(Type.GameStart),
            GameEnd(Type.GameEnd),
            PauseStart(Type.PauseStart),
            PauseEnd(Type.PauseEnd),
        }
    }

    /**
     * Represents a fault as defined by [faultType] given to [teamId]. A few of the fault events might also result in a
     * direct [Dead] event as well
     */
    data class Fault(private val baseInfo: BaseInfo, val faultType: FaultType) :
        Detailed(type = faultType.type, baseInfo = baseInfo) {

        /**
         * All types of faults. Note that some of them might not be part of the current rules but are just here for
         * backwards compatibility
         */
        enum class FaultType(val type: Type) {
            Click(Type.FaultClick),
            BackLift(Type.FaultBackLift),
            Roll(Type.FaultRoll),
            Out(Type.FaultOut),
            Catch(Type.FaultCatch),
            WicketDirect(Type.FaultWicketDirect),
            WicketShin(Type.FaultWicketShin),
            HitCatch(Type.FaultHitCatch),
        }
    }

    /**
     * Represents a switch between in and out team, the reason is defined by [switchType]. [teamId] should represent the
     * new in team
     */
    data class Switch(private val baseInfo: BaseInfo, val switchType: SwitchType) :
        Detailed(type = switchType.type, baseInfo = baseInfo) {

        /**
         * The reasons for a switch
         */
        enum class SwitchType(val type: Type) {
            Force(Type.SwitchForce),
            Time(Type.SwitchTime),
            Dead(Type.SwitchDead),
        }
    }
}
