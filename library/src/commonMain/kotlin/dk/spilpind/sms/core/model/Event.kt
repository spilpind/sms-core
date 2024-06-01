package dk.spilpind.sms.core.model

import dk.spilpind.sms.core.model.Event.Death
import dk.spilpind.sms.core.model.Event.Timing.TimingType
import kotlinx.datetime.LocalDateTime
import kotlin.jvm.JvmInline

/**
 * Representation of all possible events during a game. This does in most cases represent 1:1 what happens in the game
 * and can be used for e.g. calculating time spent in the game and points of the teams. In a few cases more than one
 * event is needed to represent a real life event. This is e.g. the case for events during the game that result in a
 * direct death as they are all represented as a fault and thus also needs a [Death] event right after. There's a few
 * reasons for this, one being that it's more generic and could be configured per game-basis if the event should result
 * in a death or just a fault.
 *
 * If specified [teamId] in most cases represents the in team. The few ambiguous cases are the events that changes or
 * "sets" the in team e.g. timing and switch events - in those cases the [teamId] represents the new in team.
 *
 * [time] represents the relative seconds within the game the event happened - this is the time you would e.g. see in a
 * score board
 */
sealed interface Event {
    val eventId: Id
    val gameId: Game.Id
    val teamId: Team.Id?
    val typeId: Int
    val time: Int
    val refereeId: User.Id
    val created: LocalDateTime

    /**
     * Id of an event. Can be used to reference an event without having to care about the remaining event data
     */
    @JvmInline
    value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

        override fun compareTo(other: Id): Int =
            identifier.compareTo(other.identifier)

    }

    /**
     * All type of events, most important part of this are their unique ids which is useful for e.g. storing the events.
     * The ids are grouped as follows:
     * - 10+: Overall game events stuff
     * - 20+: [Timing] events
     * - 30+: [Fault] events (including events that might result in a direct death)
     * - 40+: [Switch] events
     */
    enum class Type(val typeId: Int) {
        Points(11),
        Death(12),
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
        FaultCrossingDefenceLine(39),
        SwitchForce(41),
        SwitchTime(42),
        SwitchDeaths(43),
    }

    /**
     * Represents the raw event
     */
    data class Raw(
        override val eventId: Id,
        override val gameId: Game.Id,
        override val teamId: Team.Id?,
        override val typeId: Int,
        override val time: Int,
        override val refereeId: User.Id,
        override val created: LocalDateTime,
        val points: Int?
    ) : Event

    /**
     * Like [Raw], but with a specified [type]. The subclasses also represents specific type of event and might include
     * additional data, like [Points] that contains the number of points.
     */
    sealed class Simple(val type: Type, baseInfo: BaseInfo) : Event {
        override val eventId: Id = baseInfo.eventId
        override val gameId: Game.Id = baseInfo.gameId
        override val teamId: Team.Id? = baseInfo.teamId
        override val typeId = type.typeId
        override val time: Int = baseInfo.time
        override val refereeId: User.Id = baseInfo.refereeId
        override val created: LocalDateTime = baseInfo.created

        /**
         * Base information that's used for all events
         */
        data class BaseInfo(
            val eventId: Id,
            val gameId: Game.Id,
            val teamId: Team.Id?,
            val time: Int,
            val refereeId: User.Id,
            val created: LocalDateTime
        )
    }

    /**
     * Represents points given to [teamId] after a successful lift
     */
    data class Points(private val baseInfo: BaseInfo, val points: Int) :
        Simple(type = Type.Points, baseInfo = baseInfo)

    /**
     * Represents a single death given to [teamId]
     */
    data class Death(private val baseInfo: BaseInfo) : Simple(type = Type.Death, baseInfo = baseInfo)

    /**
     * Represents a lift without any death or faults by [teamId]
     */
    data class LiftSuccess(private val baseInfo: BaseInfo) : Simple(type = Type.LiftSuccess, baseInfo = baseInfo)

    /**
     * Represents a timing event indicated by [timingType]. Only [TimingType.GameStart] is expected to have a [teamId]
     * which represents the team starting as in team
     */
    data class Timing(private val baseInfo: BaseInfo, val timingType: TimingType) :
        Simple(type = timingType.type, baseInfo = baseInfo) {

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
     * direct [Death] event as well
     */
    data class Fault(private val baseInfo: BaseInfo, val faultType: FaultType) :
        Simple(type = faultType.type, baseInfo = baseInfo) {

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
            CrossingDefenceLine(Type.FaultCrossingDefenceLine),
        }
    }

    /**
     * Represents a switch between in and out team, the reason is defined by [switchType]. [teamId] represents the new
     * in team
     */
    data class Switch(private val baseInfo: BaseInfo, val switchType: SwitchType) :
        Simple(type = switchType.type, baseInfo = baseInfo) {

        /**
         * The reasons for a switch
         */
        enum class SwitchType(val type: Type) {
            Force(Type.SwitchForce),
            Time(Type.SwitchTime),
            Death(Type.SwitchDeaths),
        }
    }
}
