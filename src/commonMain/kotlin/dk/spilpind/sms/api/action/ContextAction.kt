package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.core.model.Context
import kotlinx.serialization.Serializable

/**
 * Interface for all actions for specific contexts
 */
@Serializable
sealed class ContextAction {
    abstract val context: Context
    abstract val action: Action
}
