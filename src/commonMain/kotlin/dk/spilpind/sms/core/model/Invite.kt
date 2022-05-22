package dk.spilpind.sms.core.model

import kotlinx.datetime.LocalDateTime

/**
 * Represents an invite (for a [request] in the given [context]) that needs to be accepted by one or more users. This
 * can for instance be an invite to be added as captain of a team or become the second team of a game. [contextId]
 * represents the id in the context - for instance, if context is "tournament" it is expected to be the id of the
 * tournament this invite relates to
 */
sealed interface Invite {
    val inviteId: Int
    val context: String
    val contextId: Int
    val request: String
    val code: String
    val expires: LocalDateTime
    val requesterId: Int

    /**
     * Defines all type of invites that exists
     */
    sealed class Type(context: RawContext, request: RawRequest) {
        val context = context.identifier
        val request = request.identifier

        /**
         * Defines all type of invites that exists for a game
         */
        sealed class Game(request: Requests) : Type(context = RawContext.Game, request = request) {
            enum class Requests(override val identifier: String) : RawRequest {
                Join("join")
            }

            /**
             * An invite for a team to join the game
             */
            object Join : Game(request = Requests.Join)
        }
    }

    /**
     * Represents the raw invite
     */
    data class Raw(
        override val inviteId: Int,
        override val context: String,
        override val contextId: Int,
        override val request: String,
        override val code: String,
        override val expires: LocalDateTime,
        override val requesterId: Int
    ) : Invite

    /**
     * Like [Raw], but with a specified [type]
     */
    data class Simple(
        override val inviteId: Int,
        val type: Type,
        override val contextId: Int,
        override val code: String,
        override val expires: LocalDateTime,
        override val requesterId: Int
    ) : Invite {

        override val context: String = type.context

        override val request: String = type.request

    }

    companion object {
        // TODO: Use context from api instead
        internal enum class RawContext(val identifier: String) {
            Game("game")
        }

        internal interface RawRequest {
            val identifier: String
        }

        /**
         * Finds a [Type] based on the provided parameters or throw an [IllegalArgumentException] if not found
         */
        fun findType(contextIdentifier: String, requestIdentifier: String): Type {
            val rawContext = RawContext.values().firstOrNull { context ->
                context.identifier == contextIdentifier
            }

            return when (rawContext) {
                RawContext.Game -> when (
                    findRequestOrNull<Type.Game.Requests>(identifier = requestIdentifier)
                ) {
                    Type.Game.Requests.Join -> Type.Game.Join
                    null -> throwNotFound<Type.Game.Requests>(
                        rawContext = rawContext,
                        requestIdentifier = requestIdentifier
                    )
                }
                null -> throw IllegalArgumentException(
                    "Context \"$contextIdentifier\" not found. Available contexts: " +
                            "${RawContext.values().map { availableContext -> availableContext.identifier }}"
                )
            }
        }

        private inline fun <reified RequestType> findRequestOrNull(identifier: String): RequestType?
                where RequestType : Enum<RequestType>, RequestType : RawRequest {
            return enumValues<RequestType>().firstOrNull { request -> request.identifier == identifier }
        }

        private inline fun <reified RequestType> throwNotFound(
            rawContext: RawContext,
            requestIdentifier: String
        ): Nothing where RequestType : Enum<RequestType>, RequestType : RawRequest {
            val availableRequests = enumValues<RequestType>()

            throw IllegalArgumentException(
                "Request \"$requestIdentifier\" not found in context \"${rawContext.identifier}\". " +
                        "Available requests: ${availableRequests.map { request -> request.identifier }}"
            )
        }
    }
}
