package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action

/**
 * Interface for all actions for specific contexts. The [minimumAccessLevel] restricts which users can use the action,
 * and the users access level has to be equal to that number or a lower number to be allowed to use the action
 */
sealed interface ContextAction {
    val action: Action
    val minimumAccessLevel: Int?
}
