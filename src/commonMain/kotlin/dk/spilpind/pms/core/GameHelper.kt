package dk.spilpind.pms.core

import dk.spilpind.pms.core.TimingHelper.secondsUntilNow
import dk.spilpind.pms.core.model.Event

object EventHelper {

    val List<Event>.gameTime: Int
        get() = firstNotNullOfOrNull { event ->
            when (event) {
                is Event.Dead -> null
                is Event.Fault -> null
                is Event.Points -> null
                is Event.Switch -> null
                is Event.Timing -> {
                    val eventTime = event.baseInfo.time
                    when (event.timingType) {
                        Event.Timing.TimingType.GameStart ->
                            eventTime + event.baseInfo.created.secondsUntilNow()
                        Event.Timing.TimingType.GameEnd -> eventTime
                        Event.Timing.TimingType.PauseStart -> eventTime
                        Event.Timing.TimingType.PauseEnd ->
                            eventTime + event.baseInfo.created.secondsUntilNow()
                    }
                }
            }
        } ?: 0

    val List<Event>.turnTime: Int
        get() {
            val switchTime = firstNotNullOfOrNull { event ->
                when (event) {
                    is Event.Dead -> null
                    is Event.Fault -> null
                    is Event.Points -> null
                    is Event.Switch -> event.baseInfo.time
                    is Event.Timing -> null
                }
            }

            return if (switchTime == null) {
                gameTime
            } else {
                gameTime - switchTime
            }
        }
}
