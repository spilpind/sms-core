package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.ContextAction

/**
 * All possible actions that can be made in relation to [Context.Tournament]
 */
enum class TournamentAction(override val action: Action, override val minimumAccessLevel: Int?) : ContextAction {
    Add(Action.Add, minimumAccessLevel = 2),
    Remove(Action.Remove, minimumAccessLevel = 2),
    Fetch(Action.Fetch, minimumAccessLevel = null),
}
