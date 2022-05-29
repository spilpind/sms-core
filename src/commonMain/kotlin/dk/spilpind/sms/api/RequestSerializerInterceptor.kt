package dk.spilpind.sms.api

import dk.spilpind.sms.api.RequestSerializerInterceptor.ConversionException
import dk.spilpind.sms.api.SerializationUtil.alterObjectIfExists
import dk.spilpind.sms.api.SerializationUtil.asStringOrNull
import dk.spilpind.sms.api.SerializationUtil.putType
import dk.spilpind.sms.api.SerializationUtil.removeType
import dk.spilpind.sms.api.action.*
import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.api.common.Context
import dk.spilpind.sms.api.common.Reaction
import dk.spilpind.sms.common.Localization
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject

/**
 * Intercepts the deserialization and maps the context and action to the correct [ContextAction]. If the conversion
 * could not be performed, [ConversionException] will be thrown.
 *
 * This also intercepts serialization, but that's mainly to avoid the type being outputted as well. The same idea is
 * done with [JsonContentPolymorphicSerializer], but it doesn't seem like we can use just exactly that as we need to
 * access context and action of the request.
 *
 * TODO This might be possible to handle better in the future: github.com/Kotlin/kotlinx.serialization/issues/793
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
                    Action.Add -> AuthenticationAction.Add.serializer()
                    Action.Remove -> AuthenticationAction.Remove.serializer()
                    Action.Update -> null
                    Action.Accept -> null
                    Action.Subscribe -> null
                    Action.Unsubscribe -> null
                }
                Context.User -> when (action) {
                    Action.Inform -> null
                    Action.Add -> null
                    Action.Remove -> UserAction.Remove.serializer()
                    Action.Update -> null
                    Action.Accept -> null
                    Action.Subscribe -> UserAction.Subscribe.serializer()
                    Action.Unsubscribe -> UserAction.Unsubscribe.serializer()
                }
                Context.UserRole -> when (action) {
                    Action.Inform -> null
                    Action.Add -> UserRoleAction.Add.serializer()
                    Action.Remove -> null
                    Action.Update -> null
                    Action.Accept -> null
                    Action.Subscribe -> UserRoleAction.Subscribe.serializer()
                    Action.Unsubscribe -> UserRoleAction.Unsubscribe.serializer()
                }
                Context.Tournament -> when (action) {
                    Action.Inform -> null
                    Action.Add -> TournamentAction.Add.serializer()
                    Action.Remove -> TournamentAction.Remove.serializer()
                    Action.Update -> null
                    Action.Accept -> null
                    Action.Subscribe -> TournamentAction.Subscribe.serializer()
                    Action.Unsubscribe -> TournamentAction.Unsubscribe.serializer()
                }
                Context.Game -> when (action) {
                    Action.Inform -> null
                    Action.Add -> GameAction.Add.serializer()
                    Action.Remove -> GameAction.Remove.serializer()
                    Action.Update -> null
                    Action.Accept -> GameAction.Accept.serializer()
                    Action.Subscribe -> GameAction.Subscribe.serializer()
                    Action.Unsubscribe -> GameAction.Unsubscribe.serializer()
                }
                Context.Team -> when (action) {
                    Action.Inform -> null
                    Action.Add -> TeamAction.Add.serializer()
                    Action.Remove -> TeamAction.Remove.serializer()
                    Action.Update -> null
                    Action.Accept -> null
                    Action.Subscribe -> TeamAction.Subscribe.serializer()
                    Action.Unsubscribe -> TeamAction.Unsubscribe.serializer()
                }
                Context.Referee -> when (action) {
                    Action.Inform -> null
                    Action.Add -> RefereeAction.Add.serializer()
                    Action.Remove -> RefereeAction.Remove.serializer()
                    Action.Update -> null
                    Action.Accept -> null
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
        val actionClass = findActionClass(
            context = element.jsonObject[Request.CONTEXT_SERIAL_NAME]?.asStringOrNull(),
            action = element.jsonObject[Request.ACTION_SERIAL_NAME]?.asStringOrNull(),
            actionId = element.jsonObject[Request.ACTION_ID_SERIAL_NAME]?.asStringOrNull()
        )

        return element.alterObjectIfExists(key = Request.DATA_SERIAL_NAME) {
            putType(actionClass)
        }
    }

    override fun transformSerialize(element: JsonElement): JsonElement {
        return element.alterObjectIfExists(key = Request.DATA_SERIAL_NAME) {
            // We don't want to expose internal types as they're not needed (context + action should do the trick)
            removeType()
        }
    }

    private fun findActionClass(
        context: String?,
        action: String?,
        actionId: String?
    ): String {
        val actionClassMap = contextActionClassMap[context]
        if (actionClassMap == null) {
            val availableContexts = contextActionClassMap.keys

            val response = Response(
                context = context,
                reaction = Reaction.ContextNotFound.reactionKey,
                actionId = actionId,
                data = ErrorReaction.ContextNotFound(
                    localizedMessage = Localization.Danish.unknownErrorPermanent,
                    debugMessage = "Available contexts: $availableContexts"
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
                    localizedMessage = Localization.Danish.unknownErrorPermanent,
                    debugMessage = "Available for context is: $availableActions"
                )
            )

            throw ConversionException(response = response)
        }

        return actionClass
    }
}
