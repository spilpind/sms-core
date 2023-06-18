package dk.spilpind.sms.core.model

import kotlinx.datetime.LocalDateTime
import kotlin.jvm.JvmInline

/**
 * Represents an pending [request] (in the given [context]) that needs to be accepted by one or more users. This can for
 * instance be an invite to be added as captain of a team or become the second team of a game. [contextId] represents
 * the id in the context - for instance, if context is "tournament" it is expected to be the id of the tournament this
 * request relates to
 */
sealed interface PendingRequest {
    val pendingRequestId: Id
    val context: String
    val contextId: Int
    val request: String
    val code: String
    val expires: LocalDateTime
    val requesterId: User.Id

    /**
     * Id of a pending request. Can be used to reference a pending request without having to care about the remaining
     * pending request data
     */
    @JvmInline
    value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

        override fun compareTo(other: Id): Int =
            identifier.compareTo(other.identifier)

    }

    /**
     * Defines all type of pending requests that exists
     */
    sealed class Type(context: PendingRequestContext, request: RawRequest) {
        val context = context.context
        val request = request.identifier

        /**
         * Defines all types of pending requests that exist for a game
         */
        sealed class Game(request: Requests) : Type(context = PendingRequestContext.Game, request = request) {
            enum class Requests(override val identifier: String) : RawRequest {
                TeamJoinInvite("teamJoinInvite"),
                RefereeInvite("refereeInvite")
            }

            /**
             * An invite for a team to join the game
             */
            object TeamJoinInvite : Game(request = Requests.TeamJoinInvite)

            /**
             * An invite for a user to become a referee of the game
             */
            object RefereeInvite : Game(request = Requests.TeamJoinInvite)
        }

        /**
         * Defines all types of pending requests that exist for a team
         */
        sealed class Team(request: Requests) : Type(context = PendingRequestContext.Team, request = request) {
            enum class Requests(override val identifier: String) : RawRequest {
                MemberJoinInvite("memberJoinInvite")
            }

            /**
             * An invite for a user to join the team as a member
             */
            object MemberJoinInvite : Team(request = Requests.MemberJoinInvite)
        }
    }

    /**
     * Represents the raw pending request
     */
    data class Raw(
        override val pendingRequestId: Id,
        override val context: String,
        override val contextId: Int,
        override val request: String,
        override val code: String,
        override val expires: LocalDateTime,
        override val requesterId: User.Id
    ) : PendingRequest

    /**
     * Like [Raw], but with a specified [type]
     */
    data class Simple(
        override val pendingRequestId: Id,
        val type: Type,
        override val contextId: Int,
        override val code: String,
        override val expires: LocalDateTime,
        override val requesterId: User.Id
    ) : PendingRequest {

        override val context: String = type.context.contextKey

        override val request: String = type.request

    }

    companion object {
        internal enum class PendingRequestContext(val context: Context) {
            Game(Context.Game),
            Team(Context.Team)
        }

        internal interface RawRequest {
            val identifier: String
        }

        /**
         * Finds a [Type] based on the provided parameters or throw an [IllegalArgumentException] if not found or not
         * the expected [RequestType]
         */
        inline fun <reified RequestType : Type> findExpectedType(
            contextIdentifier: String,
            requestIdentifier: String
        ): RequestType {
            val type = findType(contextIdentifier = contextIdentifier, requestIdentifier = requestIdentifier)

            if (type !is RequestType) {
                throw IllegalArgumentException("Pending request type was found, but it wasn't the expected type")
            }

            return type
        }

        /**
         * Finds a [Type] based on the provided parameters or throw an [IllegalArgumentException] if not found
         */
        fun findType(contextIdentifier: String, requestIdentifier: String): Type {
            val context = PendingRequestContext.values().firstOrNull { context ->
                context.context.contextKey == contextIdentifier
            }

            return when (context) {
                PendingRequestContext.Game -> when (
                    findRequestOrNull<Type.Game.Requests>(identifier = requestIdentifier)
                ) {
                    Type.Game.Requests.TeamJoinInvite -> Type.Game.TeamJoinInvite
                    Type.Game.Requests.RefereeInvite -> Type.Game.RefereeInvite
                    null -> throwNotFound<Type.Game.Requests>(
                        context = context,
                        requestIdentifier = requestIdentifier
                    )
                }
                PendingRequestContext.Team -> when (
                    findRequestOrNull<Type.Team.Requests>(identifier = requestIdentifier)
                ) {
                    Type.Team.Requests.MemberJoinInvite -> Type.Team.MemberJoinInvite
                    null -> throwNotFound<Type.Team.Requests>(
                        context = context,
                        requestIdentifier = requestIdentifier
                    )
                }
                null -> throw IllegalArgumentException(
                    "Context \"$contextIdentifier\" of pending request not found. Available contexts: ${
                        PendingRequestContext.values().map { availableContext -> availableContext.context.contextKey }
                    }"
                )
            }
        }

        private inline fun <reified RequestType> findRequestOrNull(identifier: String): RequestType?
                where RequestType : Enum<RequestType>, RequestType : RawRequest {
            return enumValues<RequestType>().firstOrNull { request -> request.identifier == identifier }
        }

        private inline fun <reified RequestType> throwNotFound(
            context: PendingRequestContext,
            requestIdentifier: String
        ): Nothing where RequestType : Enum<RequestType>, RequestType : RawRequest {
            val availableRequests = enumValues<RequestType>()

            throw IllegalArgumentException(
                "Pending request type \"$requestIdentifier\" not found in context \"${context.context.contextKey}\". " +
                        "Available requests: ${availableRequests.map { request -> request.identifier }}"
            )
        }
    }
}
