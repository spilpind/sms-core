package dk.spilpind.sms.api

import dk.spilpind.sms.api.SerializationUtil.alterData
import dk.spilpind.sms.api.SerializationUtil.asStringOrNull
import dk.spilpind.sms.api.action.*
import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.api.common.Context
import dk.spilpind.sms.api.common.Reaction
import dk.spilpind.sms.api.action.ErrorReaction
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*

/**
 * Intercepts the deserialization and maps the context and action to the correct [ContextAction]. If
 * the conversion could not be performed, [ConversionException] will be thrown. This might be
 * possible to handle in another way in the future:
 * https://github.com/Kotlin/kotlinx.serialization/issues/793
 *
 * This also intercepts serialization, but that's mainly to avoid the type being outputted as
 * well. The same idea is done with [JsonContentPolymorphicSerializer], but it doesn't seem like we
 * can use just exactly that as we need to access the request's context and action
 */
object RequestSerializerInterceptor : JsonTransformingSerializer<dk.spilpind.sms.api.Request>(dk.spilpind.sms.api.Request.serializer()) {

    /**
     * Exception thrown in case a conversion could not be performed. [response] can be used as response to the caller
     * to notify about what went wrong
     */
    class ConversionException(val response: dk.spilpind.sms.api.Response) : Exception()

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
        val actionId = element.jsonObject["actionId"]?.asStringOrNull()
        val context = element.jsonObject["context"]?.asStringOrNull()
        val action = element.jsonObject["action"]?.asStringOrNull()
        val actionClass = dk.spilpind.sms.api.RequestSerializerInterceptor.findActionClass(
            actionId = actionId,
            context = context,
            action = action
        )

        return element.alterData {
            put("type", JsonPrimitive(actionClass))
        }
    }

    override fun transformSerialize(element: JsonElement): JsonElement {
        return element.alterData {
            // We don't want to expose internal types as they're not needed (context + action should
            // do the trick)
            remove("type")
        }
    }

    private fun findActionClass(
        actionId: String?,
        context: String?,
        action: String?
    ): String {
        val actionClassMap = dk.spilpind.sms.api.RequestSerializerInterceptor.contextActionClassMap[context]
        if (actionClassMap == null) {
            val availableContexts = dk.spilpind.sms.api.RequestSerializerInterceptor.contextActionClassMap.keys

            val response = dk.spilpind.sms.api.Response(
                context = context,
                reaction = Reaction.ContextNotFound.reactionKey,
                actionId = actionId,
                data = ErrorReaction.ContextNotFound(
                    action = action,
                    message = "Available contexts: $availableContexts"
                ),
            )

            throw dk.spilpind.sms.api.RequestSerializerInterceptor.ConversionException(response = response)
        }

        val actionClass = actionClassMap[action]
        if (actionClass == null) {
            val availableActions = actionClassMap.keys

            val response = dk.spilpind.sms.api.Response(
                context = context,
                reaction = Reaction.ActionNotFound.reactionKey,
                actionId = actionId,
                data = ErrorReaction.ActionNotFound(
                    action = action,
                    message = "Available for context is: $availableActions"
                )
            )

            throw dk.spilpind.sms.api.RequestSerializerInterceptor.ConversionException(response = response)
        }

        return actionClass
    }
}
