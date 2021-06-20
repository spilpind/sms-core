package dk.spilpind.pms.core

import dk.spilpind.pms.core.TimingHelper.secondsUntilNow
import dk.spilpind.pms.core.model.Event
import dk.spilpind.pms.core.model.Game
import dk.spilpind.pms.core.model.Team

object GameHelper {

    enum class GameState {
        NOT_STARTED,
        STARTED,
        PAUSED,
        FINISHED
    }

    fun Game.findInTeam(events: List<Event>): Team? {
        return when (val inTeamId = events.inTeamId) {
            null -> null
            teamA?.teamId -> teamA
            teamB?.teamId -> teamB
            else -> throw IllegalStateException(
                "Could not find in team with ID $inTeamId, when trying to find in team for game with ID $gameId"
            )
        }
    }

    fun Game.findOutTeam(events: List<Event>): Team? {
        return when (val inTeamId = events.inTeamId) {
            null -> null
            teamA?.teamId -> teamB
            teamB?.teamId -> teamA
            else -> throw IllegalStateException(
                "Could not find in team with ID $inTeamId, when trying to find out team for game with ID $gameId"
            )
        }
    }

    fun Team.calculatePoints(events: List<Event>): Int {
        return events.sumOf { event ->
            when (event) {
                is Event.Dead -> 0
                is Event.Fault -> 0
                is Event.Points -> if (event.baseInfo.teamId == teamId) {
                    event.points
                } else {
                    0
                }
                is Event.Switch -> 0
                is Event.Timing -> 0
            }
        }
    }

    val List<Event>.gameState: GameState
        get() = when (val event = firstOrNull()) {
            is Event.Dead -> GameState.STARTED
            is Event.Fault -> GameState.STARTED
            is Event.Points -> GameState.STARTED
            is Event.Switch -> GameState.STARTED
            is Event.Timing -> when (event.timingType) {
                Event.Timing.TimingType.GameStart -> GameState.STARTED
                Event.Timing.TimingType.GameEnd -> GameState.FINISHED
                Event.Timing.TimingType.PauseStart -> GameState.PAUSED
                Event.Timing.TimingType.PauseEnd -> GameState.STARTED
            }
            null -> GameState.NOT_STARTED
        }

    val List<Event>.faultCount: Int
        get() {
            var count = 0

            forEach { event ->
                when (event) {
                    is Event.Dead -> return count
                    is Event.Fault -> ++count
                    is Event.Points -> return count
                    is Event.Switch -> return count
                    is Event.Timing -> when (event.timingType) {
                        Event.Timing.TimingType.GameStart -> return count
                        Event.Timing.TimingType.GameEnd -> return count
                        Event.Timing.TimingType.PauseStart -> return@forEach
                        Event.Timing.TimingType.PauseEnd -> return@forEach
                    }
                }
            }

            return count
        }

    val List<Event>.deadCount: Int
        get() {
            var count = 0

            forEach { event ->
                when (event) {
                    is Event.Dead -> ++count
                    is Event.Fault -> return@forEach
                    is Event.Points -> return@forEach
                    is Event.Switch -> return count
                    is Event.Timing -> when (event.timingType) {
                        Event.Timing.TimingType.GameStart -> return count
                        Event.Timing.TimingType.GameEnd -> return count
                        Event.Timing.TimingType.PauseStart -> return@forEach
                        Event.Timing.TimingType.PauseEnd -> return@forEach
                    }
                }
            }

            return count
        }

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

    private val List<Event>.inTeamId: Int?
        get() = firstNotNullOfOrNull { event ->
            when (event) {
                is Event.Dead -> null
                is Event.Fault -> null
                is Event.Points -> null
                is Event.Switch -> event.baseInfo.teamId
                is Event.Timing -> when (event.timingType) {
                    Event.Timing.TimingType.GameStart -> event.baseInfo.teamId
                    Event.Timing.TimingType.GameEnd -> null
                    Event.Timing.TimingType.PauseStart -> null
                    Event.Timing.TimingType.PauseEnd -> null
                }
            }
        }
}
