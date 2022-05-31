package dk.spilpind.sms.api

import dk.spilpind.sms.api.ResponseSerializerInterceptor.ConversionException
import dk.spilpind.sms.api.SerializationUtil.alterObjectIfExists
import dk.spilpind.sms.api.SerializationUtil.asStringOrNull
import dk.spilpind.sms.api.SerializationUtil.putType
import dk.spilpind.sms.api.SerializationUtil.removeType
import dk.spilpind.sms.api.action.*
import dk.spilpind.sms.api.common.Context
import dk.spilpind.sms.api.common.Reaction
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject

/**
 * Intercepts the deserialization of [Response] and maps reaction (and potentially context) to the correct
 * [ReactionData]. If the conversion could not be performed, [ConversionException] will be thrown.
 *
 * This also intercepts serialization, but that's mainly to avoid the type being outputted as well. The same idea is
 * done with [JsonContentPolymorphicSerializer], but it doesn't seem like we can use just exactly that as we need to
 * access context and reaction of the response.
 *
 * TODO This might be possible to handle better in the future: github.com/Kotlin/kotlinx.serialization/issues/793
 */
object ResponseSerializerInterceptor : JsonTransformingSerializer<Response>(Response.serializer()) {

    /**
     * Exception thrown in case a conversion could not be performed
     */
    class ConversionException(message: String) : Exception(message)

    private interface ReactionClassMap {
        fun findReactionClassOrNull(context: String?): String?

        class ContextBased(
            serializerMap: Map<Context, KSerializer<out ReactionData>>
        ) : ReactionClassMap {
            private val classSerialNameMap = serializerMap.map { (context, serializer) ->
                @OptIn(ExperimentalSerializationApi::class)
                Pair(context.contextKey, serializer.descriptor.serialName)
            }.toMap()

            override fun findReactionClassOrNull(context: String?): String? {
                return classSerialNameMap[context]
            }
        }

        class ContextLess(
            serializer: KSerializer<out ReactionData>
        ) : ReactionClassMap {
            @OptIn(ExperimentalSerializationApi::class)
            private val classSerialName = serializer.descriptor.serialName

            override fun findReactionClassOrNull(context: String?): String {
                return classSerialName
            }
        }
    }

    private val reactionContextClassMap = Reaction.values().associate { reaction ->
        reaction.reactionKey to when (reaction) {
            Reaction.Informed -> createContextMap { context ->
                when (context) {
                    Context.Authentication -> AuthenticationReaction.Informed.serializer()
                    Context.User -> null
                    Context.UserRole -> null
                    Context.Tournament -> null
                    Context.Game -> null
                    Context.Team -> null
                    Context.Referee -> null
                }
            }
            Reaction.Added -> createContextMap { context ->
                when (context) {
                    Context.Authentication -> AuthenticationReaction.Added.serializer()
                    Context.User -> UserReaction.Added.serializer()
                    Context.UserRole -> UserRoleReaction.Added.serializer()
                    Context.Tournament -> TournamentReaction.Added.serializer()
                    Context.Game -> GameReaction.Added.serializer()
                    Context.Team -> TeamReaction.Added.serializer()
                    Context.Referee -> null
                }
            }
            Reaction.Removed -> createContextMap { context ->
                when (context) {
                    Context.Authentication -> AuthenticationReaction.Removed.serializer()
                    Context.User -> UserReaction.Removed.serializer()
                    Context.UserRole -> null
                    Context.Tournament -> TournamentReaction.Removed.serializer()
                    Context.Game -> GameReaction.Removed.serializer()
                    Context.Team -> TeamReaction.Removed.serializer()
                    Context.Referee -> null
                }
            }
            Reaction.Updated -> createContextMap { context ->
                when (context) {
                    Context.Authentication -> null
                    Context.User -> UserReaction.Updated.serializer()
                    Context.UserRole -> UserRoleReaction.Updated.serializer()
                    Context.Tournament -> TournamentReaction.Updated.serializer()
                    Context.Game -> GameReaction.Updated.serializer()
                    Context.Team -> TeamReaction.Updated.serializer()
                    Context.Referee -> RefereeReaction.Updated.serializer()
                }
            }
            Reaction.Accepted -> createContextMap { context ->
                when (context) {
                    Context.Authentication -> null
                    Context.User -> null
                    Context.UserRole -> null
                    Context.Tournament -> null
                    Context.Game -> GameReaction.Accepted.serializer()
                    Context.Team -> null
                    Context.Referee -> null
                }
            }
            Reaction.Subscribed -> createContextMap { context ->
                when (context) {
                    Context.Authentication -> null
                    Context.User -> UserReaction.Subscribed.serializer()
                    Context.UserRole -> UserRoleReaction.Subscribed.serializer()
                    Context.Tournament -> TournamentReaction.Subscribed.serializer()
                    Context.Game -> GameReaction.Subscribed.serializer()
                    Context.Team -> TeamReaction.Subscribed.serializer()
                    Context.Referee -> RefereeReaction.Subscribed.serializer()
                }
            }
            Reaction.Unsubscribed -> createContextMap { context ->
                when (context) {
                    Context.Authentication -> null
                    Context.User -> UserReaction.Unsubscribed.serializer()
                    Context.UserRole -> UserRoleReaction.Unsubscribed.serializer()
                    Context.Tournament -> TournamentReaction.Unsubscribed.serializer()
                    Context.Game -> GameReaction.Unsubscribed.serializer()
                    Context.Team -> TeamReaction.Unsubscribed.serializer()
                    Context.Referee -> RefereeReaction.Unsubscribed.serializer()
                }
            }
            Reaction.ServerError ->
                ReactionClassMap.ContextLess(ErrorReaction.ServerError.serializer())
            Reaction.RequestStructureError ->
                ReactionClassMap.ContextLess(ErrorReaction.RequestStructureError.serializer())
            Reaction.RequestTypeError ->
                ReactionClassMap.ContextLess(ErrorReaction.RequestTypeError.serializer())
            Reaction.DataValueError ->
                ReactionClassMap.ContextLess(ErrorReaction.DataValueError.serializer())
            Reaction.UnsafeOperation ->
                ReactionClassMap.ContextLess(ErrorReaction.UnsafeOperation.serializer())
            Reaction.MissingPermission ->
                ReactionClassMap.ContextLess(ErrorReaction.MissingPermission.serializer())
            Reaction.ItemNotFound ->
                ReactionClassMap.ContextLess(ErrorReaction.ItemNotFound.serializer())
        }
    }

    override fun transformDeserialize(element: JsonElement): JsonElement {
        val context = element.jsonObject[Response.CONTEXT_SERIAL_NAME]?.asStringOrNull()
        val reaction = element.jsonObject[Response.REACTION_SERIAL_NAME]?.asStringOrNull()
        val actionClass = findReactionClass(context = context, reaction = reaction)

        return element.alterObjectIfExists(key = Response.DATA_SERIAL_NAME) {
            putType(actionClass)
        }
    }

    override fun transformSerialize(element: JsonElement): JsonElement {
        return element.alterObjectIfExists(key = Response.DATA_SERIAL_NAME) {
            // We don't want to expose internal types as they're not needed (context + reaction should do the trick)
            removeType()
        }
    }

    private fun findReactionClass(
        context: String?,
        reaction: String?
    ): String {
        val classMap = reactionContextClassMap[reaction]
            ?: throw ConversionException(message = "Unknown reaction: \"$reaction\" (context \"$context\")")

        return classMap.findReactionClassOrNull(context)
            ?: throw ConversionException(
                message = "Unknown context \"$context\" for reaction \"$reaction\""
            )
    }

    private fun createContextMap(
        mapper: (Context) -> KSerializer<out ReactionData>?
    ): ReactionClassMap {
        return ReactionClassMap.ContextBased(
            serializerMap = Context.values().mapNotNull { context ->
                val serializer = mapper(context) ?: return@mapNotNull null
                Pair(context, serializer)
            }.toMap()
        )
    }
}
