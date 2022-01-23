package dk.spilpind.pms.api.common

import dk.spilpind.pms.api.common.Action

interface ContextAction {
    val action: Action
    val minimumAccessLevel: Int? // The users access level has to be this or a lower number be allowed using the action
}
