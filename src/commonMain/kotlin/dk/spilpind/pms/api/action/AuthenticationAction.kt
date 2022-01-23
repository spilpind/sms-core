package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.ContextAction

enum class AuthenticationAction(override val action: Action, override val minimumAccessLevel: Int?) : ContextAction {
    Inform(Action.Inform, minimumAccessLevel = null),
    Fetch(Action.Fetch, minimumAccessLevel = null),
}

