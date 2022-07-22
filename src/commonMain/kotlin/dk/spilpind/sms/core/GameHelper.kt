package dk.spilpind.sms.core

import dk.spilpind.sms.core.TimeHelper.secondsUntilNow
import dk.spilpind.sms.core.model.Event
import dk.spilpind.sms.core.model.Game
import dk.spilpind.sms.core.model.Team

/**
 * Various utility functionality useful when dealing with overall game logic
 */
object GameHelper {

    /**
     * Finds the in team of the game based on the provided [events]
     */
    fun Game.Detailed.findInTeam(events: List<Event.Simple>): Team? {
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
    fun Game.Detailed.findOutTeam(events: List<Event.Simple>): Team? {
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
    fun Team.calculatePoints(events: List<Event.Simple>): Int {
        return events.sumOf { event ->
            when (event) {
                is Event.Death -> 0
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
    val List<Event.Simple>.gameState: Game.State
        get() = when (val event = firstOrNull()) {
            is Event.Death -> Game.State.STARTED
            is Event.Fault -> Game.State.STARTED
            is Event.LiftSuccess -> Game.State.STARTED
            is Event.Points -> Game.State.STARTED
            is Event.Switch -> Game.State.STARTED
            is Event.Timing -> when (event.timingType) {
                Event.Timing.TimingType.GameStart -> Game.State.STARTED
                Event.Timing.TimingType.GameEnd -> Game.State.FINISHED
                Event.Timing.TimingType.PauseStart -> Game.State.PAUSED
                Event.Timing.TimingType.PauseEnd -> Game.State.STARTED
            }
            null -> Game.State.NOT_STARTED
        }

    /**
     * Returns the fault count for the in team based on the list of events
     */
    val List<Event.Simple>.faultCount: Int
        get() {
            var count = 0

            forEach { event ->
                count += when (event) {
                    is Event.Death -> return count
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
     * Returns the number of deaths for the in team based on the list of events
     */
    val List<Event.Simple>.deathCount: Int
        get() {
            var count = 0

            forEach { event ->
                count += when (event) {
                    is Event.Death -> 1
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
    val List<Event.Simple>.isLiftSuccessFull: Boolean
        get() = firstNotNullOfOrNull { event ->
            when (event) {
                is Event.Death -> false
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
    val List<Event.Simple>.gameTime: Int
        get() = firstNotNullOfOrNull { event ->
            when (event) {
                is Event.Death -> null
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
    val List<Event.Simple>.turnTime: Int
        get() = gameTime - switchTime

    /**
     * Time of last event of game, not including pauses
     */
    val List<Event.Simple>.lastEventTimeForGame: Int
        get() = firstNotNullOfOrNull { event ->
            when (event) {
                is Event.Death -> event.time
                is Event.Fault -> event.time
                is Event.LiftSuccess -> event.time
                is Event.Points -> event.time
                is Event.Switch -> event.time
                is Event.Timing -> when (event.timingType) {
                    Event.Timing.TimingType.GameStart -> event.time
                    Event.Timing.TimingType.GameEnd -> event.time
                    Event.Timing.TimingType.PauseStart -> null
                    Event.Timing.TimingType.PauseEnd -> null
                }
            }
        } ?: 0

    /**
     * Time of last event with respect to last switch (thus of this turn), not including pauses
     */
    val List<Event.Simple>.lastEventTimeForTurn: Int
        get() = firstNotNullOfOrNull { event ->
            when (event) {
                is Event.Death -> event.time
                is Event.Fault -> event.time
                is Event.LiftSuccess -> event.time
                is Event.Points -> event.time
                is Event.Switch -> event.time
                is Event.Timing -> when (event.timingType) {
                    Event.Timing.TimingType.GameStart -> event.time
                    Event.Timing.TimingType.GameEnd -> event.time
                    Event.Timing.TimingType.PauseStart -> null
                    Event.Timing.TimingType.PauseEnd -> null
                }
            }
        }?.minus(switchTime) ?: 0

    private val List<Event.Simple>.switchTime: Int
        get() = firstNotNullOfOrNull { event ->
            when (event) {
                is Event.Death -> null
                is Event.Fault -> null
                is Event.LiftSuccess -> null
                is Event.Points -> null
                is Event.Switch -> event.time
                is Event.Timing -> null
            }
        } ?: 0

    private val List<Event.Simple>.inTeamId: Int?
        get() = firstNotNullOfOrNull { event ->
            when (event) {
                is Event.Death -> null
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
