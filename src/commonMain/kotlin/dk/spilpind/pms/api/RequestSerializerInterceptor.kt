package dk.spilpind.pms.api

import dk.spilpind.pms.api.action.*
import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction
import dk.spilpind.pms.api.action.ErrorReaction
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*

/**
 * Intercepts the deserialization and maps the context and action to the correct [ContextAction]. If
 * the conversion could not be performed, [ConversionException] will be thrown. This might be
 * possible to handle in another way in the future:
 * https://github.com/Kotlin/kotlinx.serialization/issues/793
 */
object RequestSerializerInterceptor : JsonTransformingSerializer<Request>(Request.serializer()) {

    /**
     * Exception thrown in case a conversion could not be performed. [response] can be used as response to the caller
     * to notify about what went wrong
     */
    class ConversionException(val response: Response) : Exception()

    private val contextActionClassMap = Context.values().associate { context ->
        context.contextKey to Action.values().mapNotNull { action ->
            val actionClass: KSerializer<out ContextAction>? = when (context) {
                Context.Authentication -> when (action) {
                    Action.Inform -> AuthenticationAction.Inform.serializer()
                    Action.Add -> null
                    Action.Remove -> null
                    Action.Update -> null
                    Action.Fetch -> AuthenticationAction.Fetch.serializer()
                    Action.Subscribe -> null
                    Action.Unsubscribe -> null
                }
                Context.User -> when (action) {
                    Action.Inform -> null
                    Action.Add -> UserAction.Add.serializer()
                    Action.Remove -> UserAction.Remove.serializer()
                    Action.Update -> null
                    Action.Fetch -> UserAction.Fetch.serializer()
                    Action.Subscribe -> null
                    Action.Unsubscribe -> null
                }
                Context.Tournament -> when (action) {
                    Action.Inform -> null
                    Action.Add -> TournamentAction.Add.serializer()
                    Action.Remove -> TournamentAction.Remove.serializer()
                    Action.Update -> null
                    Action.Fetch -> TournamentAction.Fetch.serializer()
                    Action.Subscribe -> null
                    Action.Unsubscribe -> null
                }
                Context.Game -> when (action) {
                    Action.Inform -> null
                    Action.Add -> GameAction.Add.serializer()
                    Action.Remove -> GameAction.Remove.serializer()
                    Action.Update -> null
                    Action.Fetch -> GameAction.Fetch.serializer()
                    Action.Subscribe -> null
                    Action.Unsubscribe -> null
                }
                Context.Team -> when (action) {
                    Action.Inform -> null
                    Action.Add -> TeamAction.Add.serializer()
                    Action.Remove -> TeamAction.Remove.serializer()
                    Action.Update -> null
                    Action.Fetch -> TeamAction.Fetch.serializer()
                    Action.Subscribe -> null
                    Action.Unsubscribe -> null
                }
                Context.Referee -> when (action) {
                    Action.Inform -> null
                    Action.Add -> RefereeAction.Add.serializer()
                    Action.Remove -> RefereeAction.Remove.serializer()
                    Action.Update -> null
                    Action.Fetch -> null
                    Action.Subscribe -> RefereeAction.Subscribe.serializer()
                    Action.Unsubscribe -> RefereeAction.Unsubscribe.serializer()
                }
            }

            actionClass ?: return@mapNotNull null

            @OptIn(ExperimentalSerializationApi::class)
            action.actionKey to actionClass.descriptor.serialName
        }.toMap()
    }

    override fun transformDeserialize(element: JsonElement): JsonElement {
        val actionId = element.jsonObject["actionId"]?.asStringOrNull()
        val context = element.jsonObject["context"]?.asStringOrNull()
        val action = element.jsonObject["action"]?.asStringOrNull()
        val actionClass = findActionClass(actionId = actionId, context = context, action = action)

        val data = element.jsonObject["data"]?.jsonObject?.toMutableMap()?.apply {
            put("type", JsonPrimitive(actionClass))
        }

        return if (data == null) {
            element
        } else {
            JsonObject(element.jsonObject.toMutableMap().apply {
                put("data", JsonObject(data))
            })
        }
    }

    private fun findActionClass(
        actionId: String?,
        context: String?,
        action: String?
    ): String {
        val actionClassMap = contextActionClassMap[context]
        if (actionClassMap == null) {
            val availableContexts = contextActionClassMap.keys

            val response = Response(
                context = context,
                reaction = Reaction.ContextNotFound.reactionKey,
                actionId = actionId,
                data = ErrorReaction.ContextNotFound(
                    action = action,
                    message = "Available contexts: $availableContexts"
                ),
            )

            throw ConversionException(response = response)
        }

        val actionClass = actionClassMap[action]
        if (actionClass == null) {
            val availableActions = actionClassMap.keys

            val response = Response(
                context = context,
                reaction = Reaction.ActionNotFound.reactionKey,
                actionId = actionId,
                data = ErrorReaction.ActionNotFound(
                    action = action,
                    message = "Available for context is: $availableActions"
                )
            )

            throw ConversionException(response = response)
        }

        return actionClass
    }

    private fun JsonElement.asStringOrNull(): String? {
        return if (jsonPrimitive.isString) {
            jsonPrimitive.content
        } else {
            null
        }
    }
}
