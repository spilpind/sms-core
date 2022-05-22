package dk.spilpind.sms.core.model

/**
 * Various methods that's useful when dealing with the core models
 */
object ModelHelper {

    /**
     * Converts a raw event to a simple event
     */
    fun Event.Raw.toDetailedEvent(): Event.Simple {
        val baseInfo = Event.Simple.BaseInfo(
            eventId = eventId,
            gameId = gameId,
            teamId = teamId,
            time = time,
            refereeId = refereeId,
            created = created
        )

        val type = Event.Type.values().firstOrNull { type -> type.typeId == typeId }
            ?: throw IllegalStateException("Got unknown event type: $typeId")

        return when (type) {
            Event.Type.Points -> Event.Points(baseInfo = baseInfo, points = points ?: 0) // TODO - Log if null points
            Event.Type.Dead -> Event.Dead(baseInfo = baseInfo)
            Event.Type.LiftSuccess -> Event.LiftSuccess(baseInfo = baseInfo)
            Event.Type.GameStart -> Event.Timing(baseInfo = baseInfo, timingType = Event.Timing.TimingType.GameStart)
            Event.Type.GameEnd -> Event.Timing(baseInfo = baseInfo, timingType = Event.Timing.TimingType.GameEnd)
            Event.Type.PauseStart -> Event.Timing(baseInfo = baseInfo, timingType = Event.Timing.TimingType.PauseStart)
            Event.Type.PauseEnd -> Event.Timing(baseInfo = baseInfo, timingType = Event.Timing.TimingType.PauseEnd)
            Event.Type.FaultClick -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.Click)
            Event.Type.FaultBackLift -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.BackLift)
            Event.Type.FaultRoll -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.Roll)
            Event.Type.FaultOut -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.Out)
            Event.Type.FaultCatch -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.Catch)
            Event.Type.FaultWicketDirect ->
                Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.WicketDirect)
            Event.Type.FaultWicketShin -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.WicketShin)
            Event.Type.FaultHitCatch -> Event.Fault(baseInfo = baseInfo, faultType = Event.Fault.FaultType.HitCatch)
            Event.Type.SwitchForce -> Event.Switch(baseInfo = baseInfo, switchType = Event.Switch.SwitchType.Force)
            Event.Type.SwitchTime -> Event.Switch(baseInfo = baseInfo, switchType = Event.Switch.SwitchType.Time)
            Event.Type.SwitchDead -> Event.Switch(baseInfo = baseInfo, switchType = Event.Switch.SwitchType.Dead)
        }
    }
}
