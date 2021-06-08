package dk.spilpind.pms.core.model

import kotlinx.datetime.LocalDateTime

/**
 * Type ids are grouped as follows:
 * - 10+: overall stuff
 * - 20+: [Timing]
 * - 30+: [Fault]
 * - 40+: [Switch]
 */
sealed interface Event {

    enum class Type(val typeId: Int) {
        Points(11),
        Dead(12),
        GameStart(21),
        GameEnd(22),
        PauseStart(25),
        PauseEnd(26),
        FaultClick(31),
        FaultBacklift(32),
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

    data class BaseInfo(
        val eventId: Int?,
        val gameId: Int,
        val teamId: Int?,
        val time: Int,
        val refereeId: Int,
        val created: LocalDateTime
    )

    val type: Type
    val baseInfo: BaseInfo

    data class Points(override val baseInfo: BaseInfo, val points: Int) : Event {
        override val type: Type = Type.Points
    }

    data class Dead(override val baseInfo: BaseInfo) : Event {
        override val type: Type = Type.Dead
    }

    data class Timing(override val baseInfo: BaseInfo, val timingType: TimingType) : Event {
        override val type: Type = timingType.type

        enum class TimingType(val type: Type) {
            GameStart(Type.GameStart),
            GameEnd(Type.GameEnd),
            PauseStart(Type.PauseStart),
            PauseEnd(Type.PauseEnd),
        }
    }

    data class Fault(override val baseInfo: BaseInfo, val faultType: FaultType) : Event {
        override val type: Event.Type = faultType.type

        enum class FaultType(val type: Type) {
            Click(Type.FaultClick),
            Backlift(Type.FaultBacklift),
            Roll(Type.FaultRoll),
            Out(Type.FaultOut),
            Catch(Type.FaultCatch),
            WicketDirect(Type.FaultWicketDirect),
            WicketShin(Type.FaultWicketShin),
            HitCatch(Type.FaultHitCatch),
        }
    }

    data class Switch(override val baseInfo: BaseInfo, val switchType: SwitchType) : Event {
        override val type: Type = switchType.type

        enum class SwitchType(val type: Type) {
            Force(Type.SwitchForce),
            Time(Type.SwitchTime),
            Dead(Type.SwitchDead),
        }
    }
}
