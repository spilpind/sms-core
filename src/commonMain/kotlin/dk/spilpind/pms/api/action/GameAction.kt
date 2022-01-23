package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.ContextAction

enum class GameAction(override val action: Action, override val minimumAccessLevel: Int?) : ContextAction {
    Add(Action.Add, minimumAccessLevel = 2),
    Remove(Action.Remove, minimumAccessLevel = 2),
    Fetch(Action.Fetch, minimumAccessLevel = null),
}

