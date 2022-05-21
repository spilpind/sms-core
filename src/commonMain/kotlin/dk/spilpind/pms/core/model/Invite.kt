package dk.spilpind.pms.core.model

import kotlinx.datetime.LocalDateTime

/**
 * Represents an invite that needs to be accepted by one or more users. This can for instance be an invite to be added
 * as captain of a team or become the second team of a game
 */
sealed interface Invite {
    val inviteId: Int
    val context: String
    val contextId: Int
    val action: String
    val code: String
    val expires: LocalDateTime
    val requesterId: Int

    /**
     * Defines all types of invites that exists
     */
    sealed class ContextAction(context: RawContext, action: RawAction) {
        val context = context.identifier
        val action = action.identifier

        /**
         * Defines all types of invites that exists for a game
         */
        sealed class Game(action: Actions) : ContextAction(context = RawContext.Game, action = action) {
            enum class Actions(override val identifier: String) : RawAction {
                Join("join")
            }

            /**
             * An invite for a team to join the game
             */
            object Join : Game(action = Actions.Join)
        }
    }

    /**
     * Represents the raw invite
     */
    data class Raw(
        override val inviteId: Int,
        override val context: String,
        override val contextId: Int,
        override val action: String,
        override val code: String,
        override val expires: LocalDateTime,
        override val requesterId: Int
    ) : Invite

    /**
     * Like [Raw], but with a specified [contextAction]
     */
    data class Simple(
        override val inviteId: Int,
        val contextAction: ContextAction,
        override val contextId: Int,
        override val code: String,
        override val expires: LocalDateTime,
        override val requesterId: Int
    ) : Invite {

        override val context: String = contextAction.context

        override val action: String = contextAction.action

    }

    companion object {
        // TODO: Use context from pms-core instead
        internal enum class RawContext(val identifier: String) {
            Game("game")
        }

        internal interface RawAction {
            val identifier: String
        }

        /**
         * Finds a [ContextAction] based on the provided parameters or throw an [IllegalArgumentException] if not found
         */
        fun findContextAction(contextIdentifier: String, actionIdentifier: String): ContextAction {
            val rawContext = RawContext.values().firstOrNull { context ->
                context.identifier == contextIdentifier
            }

            return when (rawContext) {
                RawContext.Game -> when (ContextAction.Game.Actions.values()
                    .findActionOrNull(identifier = actionIdentifier)) {
                    ContextAction.Game.Actions.Join -> ContextAction.Game.Join
                    null -> throwArgumentException(
                        rawContext = rawContext,
                        actionIdentifier = actionIdentifier,
                        availableActions = ContextAction.Game.Actions.values()
                    )
                }
                null -> throw IllegalArgumentException(
                    "Context \"$contextIdentifier\" not found. Available contexts: " +
                            "${RawContext.values().map { availableContext -> availableContext.identifier }}"
                )
            }
        }

        private fun <T> Array<T>.findActionOrNull(identifier: String): T? where T : Enum<T>, T : RawAction {
            return firstOrNull { action -> action.identifier == identifier }
        }

        private fun <T, R> throwArgumentException(
            rawContext: RawContext, actionIdentifier: String, availableActions: Array<T>
        ): R where T : Enum<T>, T : RawAction {
            throw IllegalArgumentException(
                "Action \"$actionIdentifier\" not found en context \"${rawContext.identifier}\". Available actions: " +
                        "${availableActions.map { availableAction -> availableAction.identifier }}"
            )
        }
    }
}
