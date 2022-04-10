package dk.spilpind.pms.core

import dk.spilpind.pms.core.TimeHelper.secondsUntilNow
import dk.spilpind.pms.core.model.Event
import dk.spilpind.pms.core.model.Game
import dk.spilpind.pms.core.model.Team

/**
 * Various utility functionality useful when dealing with overall game logic
 */
object GameHelper {

    /**
     * Represents available states of a game. [NOT_STARTED] is expected if the game hasn't started and [FINISHED] is to
     * be used in case the game is done. [STARTED] is when the game is active and [PAUSED] represents when the game (and
     * time) has temporarily been stopped. This means [NOT_STARTED], [STARTED] and [FINISHED] are always expected at
     * some point during a game (and in that order) and [PAUSED] should only exist between two [STARTED] states
     */
    enum class GameState {
        NOT_STARTED, STARTED, PAUSED, FINISHED
    }

    /**
     * Finds the in team of the game based on the provided [events]
     */
    fun Game.Detailed.findInTeam(events: List<Event.Detailed>): Team? {
        return when (val inTeamId = events.inTeamId) {
            null -> null
            teamA?.teamId -> teamA
            teamB?.teamId -> teamB
            else -> throw IllegalStateException(
                "Could not find in team with ID $inTeamId, when trying to find in team for game with ID $gameId"
            )
        }
    }

    /**
     * Finds the out team of the game based on the provided [events]
     */
    fun Game.Detailed.findOutTeam(events: List<Event.Detailed>): Team? {
        return when (val inTeamId = events.inTeamId) {
            null -> null
            teamA?.teamId -> teamB
            teamB?.teamId -> teamA
            else -> throw IllegalStateException(
                "Could not find in team with ID $inTeamId, when trying to find out team for game with ID $gameId"
            )
        }
    }

    /**
     * Calculates the points for the team based on the provided [events]
     */
    fun Team.calculatePoints(events: List<Event.Detailed>): Int {
        return events.sumOf { event ->
            when (event) {
                is Event.Dead -> 0
                is Event.Fault -> 0
                is Event.LiftSuccess -> 0
                is Event.Points -> if (event.teamId == teamId) {
                    event.points
                } else {
                    0
                }
                is Event.Switch -> 0
                is Event.Timing -> 0
            }
        }
    }

    /**
     * Returns the game state based on the list of events
     */
    val List<Event.Detailed>.gameState: GameState
        get() = when (val event = firstOrNull()) {
            is Event.Dead -> GameState.STARTED
            is Event.Fault -> GameState.STARTED
            is Event.LiftSuccess -> GameState.STARTED
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

    /**
     * Returns the fault count for the in team based on the list of events
     */
    val List<Event.Detailed>.faultCount: Int
        get() {
            var count = 0

            forEach { event ->
                count += when (event) {
                    is Event.Dead -> return count
                    is Event.Fault -> 1
                    is Event.LiftSuccess -> 0
                    is Event.Points -> return count
                    is Event.Switch -> return count
                    is Event.Timing -> when (event.timingType) {
                        Event.Timing.TimingType.GameStart -> return count
                        Event.Timing.TimingType.GameEnd -> return count
                        Event.Timing.TimingType.PauseStart -> 0
                        Event.Timing.TimingType.PauseEnd -> 0
                    }
                }
            }

            return count
        }

    /**
     * Returns the dead count for the in team based on the list of events
     */
    val List<Event.Detailed>.deadCount: Int
        get() {
            var count = 0

            forEach { event ->
                count += when (event) {
                    is Event.Dead -> 1
                    is Event.Fault -> 0
                    is Event.LiftSuccess -> 0
                    is Event.Points -> 0
                    is Event.Switch -> return count
                    is Event.Timing -> when (event.timingType) {
                        Event.Timing.TimingType.GameStart -> return count
                        Event.Timing.TimingType.GameEnd -> return count
                        Event.Timing.TimingType.PauseStart -> 0
                        Event.Timing.TimingType.PauseEnd -> 0
                    }
                }
            }

            return count
        }

    /**
     * Indicates if the game is in a state where the in team has had a lift without faults or deaths but the defence
     * hasn't completed yet, based on the list of events. This is useful e.g. to be able to show less buttons in a
     * referee view, as there's less possible actions. The state is independent of pauses which are ignored
     */
    val List<Event.Detailed>.isLiftSuccessFull: Boolean
        get() = firstNotNullOfOrNull { event ->
            when (event) {
                is Event.Dead -> false
                is Event.Fault -> null // In theory there could be a fault after successful lift (if the rules allow it)
                is Event.LiftSuccess -> true
                is Event.Points -> false
                is Event.Switch -> false
                is Event.Timing -> when (event.timingType) {
                    Event.Timing.TimingType.GameStart -> false
                    Event.Timing.TimingType.GameEnd -> false
                    Event.Timing.TimingType.PauseStart -> null
                    Event.Timing.TimingType.PauseEnd -> null
                }
            }
        } ?: false

    /**
     * Returns the total game time in seconds, excluding pauses
     */
    val List<Event.Detailed>.gameTime: Int
        get() = firstNotNullOfOrNull { event ->
            when (event) {
                is Event.Dead -> null
                is Event.Fault -> null
                is Event.LiftSuccess -> null
                is Event.Points -> null
                is Event.Switch -> null
                is Event.Timing -> {
                    val eventTime = event.time
                    when (event.timingType) {
                        Event.Timing.TimingType.GameStart -> eventTime + event.created.secondsUntilNow()
                        Event.Timing.TimingType.GameEnd -> eventTime
                        Event.Timing.TimingType.PauseStart -> eventTime
                        Event.Timing.TimingType.PauseEnd -> eventTime + event.created.secondsUntilNow()
                    }
                }
            }
        } ?: 0

    /**
     * Calculates the turn time in seconds. This is the time since last switch or start of game, excluding pauses
     */
    val List<Event.Detailed>.turnTime: Int
        get() {
            val switchTime = firstNotNullOfOrNull { event ->
                when (event) {
                    is Event.Dead -> null
                    is Event.Fault -> null
                    is Event.LiftSuccess -> null
                    is Event.Points -> null
                    is Event.Switch -> event.time
                    is Event.Timing -> null
                }
            }

            return if (switchTime == null) {
                gameTime
            } else {
                gameTime - switchTime
            }
        }

    private val List<Event.Detailed>.inTeamId: Int?
        get() = firstNotNullOfOrNull { event ->
            when (event) {
                is Event.Dead -> null
                is Event.Fault -> null
                is Event.LiftSuccess -> null
                is Event.Points -> null
                is Event.Switch -> event.teamId
                is Event.Timing -> when (event.timingType) {
                    Event.Timing.TimingType.GameStart -> event.teamId
                    Event.Timing.TimingType.GameEnd -> null
                    Event.Timing.TimingType.PauseStart -> null
                    Event.Timing.TimingType.PauseEnd -> null
                }
            }
        }
}
