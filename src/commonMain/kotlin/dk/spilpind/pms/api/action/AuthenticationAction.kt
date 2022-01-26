package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.ContextAction
import dk.spilpind.pms.api.common.Context

/**
 * All possible actions that can be made in relation to [Context.Authentication]
 */
enum class AuthenticationAction(override val action: Action, override val minimumAccessLevel: Int?) : ContextAction {
    Inform(Action.Inform, minimumAccessLevel = null),
    Fetch(Action.Fetch, minimumAccessLevel = null),
}
