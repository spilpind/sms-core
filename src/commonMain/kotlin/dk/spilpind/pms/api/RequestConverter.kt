package dk.spilpind.pms.api

import dk.spilpind.pms.api.action.*
import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.Reaction
import dk.spilpind.pms.api.data.ErrorData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*

/**
 * Converts [Request]s to various other classes
 */
object RequestConverter {

    /**
     * Exception thrown in case a conversion could not be performed. [response] can be used as response to the caller
     * to notify about what went wrong
     */
    class ConversionException(val response: Response) : Exception()

    private val contextActionConverterMap = Context.values().associate { context ->
        context.contextKey to Action.values().mapNotNull { action ->
            val actionConverter: ((JsonObject) -> ContextAction)? = when (context) {
                Context.Authentication -> when (action) {
                    Action.Inform -> { data ->
                        Json.decodeFromJsonElement<AuthenticationAction.Inform>(data)
                    }
                    Action.Add -> null
                    Action.Remove -> null
                    Action.Update -> null
                    Action.Fetch -> { data ->
                        Json.decodeFromJsonElement<AuthenticationAction.Fetch>(data)
                    }
                    Action.Subscribe -> null
                    Action.Unsubscribe -> null
                }
                Context.User -> when (action) {
                    Action.Inform -> null
                    Action.Add -> { data ->
                        Json.decodeFromJsonElement<UserAction.Add>(data)
                    }
                    Action.Remove -> { data ->
                        Json.decodeFromJsonElement<UserAction.Remove>(data)
                    }
                    Action.Update -> null
                    Action.Fetch -> { data ->
                        Json.decodeFromJsonElement<UserAction.Fetch>(data)
                    }
                    Action.Subscribe -> null
                    Action.Unsubscribe -> null
                }
                Context.Tournament -> when (action) {
                    Action.Inform -> null
                    Action.Add -> { data ->
                        Json.decodeFromJsonElement<TournamentAction.Add>(data)
                    }
                    Action.Remove -> { data ->
                        Json.decodeFromJsonElement<TournamentAction.Remove>(data)
                    }
                    Action.Update -> null
                    Action.Fetch -> { data ->
                        Json.decodeFromJsonElement<TournamentAction.Fetch>(data)
                    }
                    Action.Subscribe -> null
                    Action.Unsubscribe -> null
                }
                Context.Game -> when (action) {
                    Action.Inform -> null
                    Action.Add -> { data ->
                        Json.decodeFromJsonElement<GameAction.Add>(data)
                    }
                    Action.Remove -> { data ->
                        Json.decodeFromJsonElement<GameAction.Remove>(data)
                    }
                    Action.Update -> null
                    Action.Fetch -> { data ->
                        Json.decodeFromJsonElement<GameAction.Fetch>(data)
                    }
                    Action.Subscribe -> null
                    Action.Unsubscribe -> null
                }
                Context.Team -> when (action) {
                    Action.Inform -> null
                    Action.Add -> { data ->
                        Json.decodeFromJsonElement<TeamAction.Add>(data)
                    }
                    Action.Remove -> { data ->
                        Json.decodeFromJsonElement<TeamAction.Remove>(data)
                    }
                    Action.Update -> null
                    Action.Fetch -> { data ->
                        Json.decodeFromJsonElement<TeamAction.Fetch>(data)
                    }
                    Action.Subscribe -> null
                    Action.Unsubscribe -> null
                }
                Context.Referee -> when (action) {
                    Action.Inform -> null
                    Action.Add -> { data ->
                        Json.decodeFromJsonElement<RefereeAction.Add>(data)
                    }
                    Action.Remove -> { data ->
                        Json.decodeFromJsonElement<RefereeAction.Remove>(data)
                    }
                    Action.Update -> null
                    Action.Fetch -> null
                    Action.Subscribe -> { data ->
                        Json.decodeFromJsonElement<RefereeAction.Subscribe>(data)
                    }
                    Action.Unsubscribe -> { data ->
                        Json.decodeFromJsonElement<RefereeAction.Unsubscribe>(data)
                    }
                }
            }

            actionConverter ?: return@mapNotNull null

            action.actionKey to actionConverter
        }.toMap()
    }

    /**
     * Returns the context and action that this request represents. If the conversion from request could not be
     * performed, [ConversionException] will be thrown
     */
    fun Request.getContextActionOrThrow(): ContextAction {
        val converterMap = getActionConverterMapOrThrow()

        return convertToActionOrThrow(converterMap)
    }

    /**
     * Creates a [Response] based on this [Request] and the provided parameters
     */
    fun Request.createResponse(reaction: Reaction, data: JsonObject) = Response(
        context = context,
        reaction = reaction.reactionKey,
        actionId = actionId,
        data = data
    )

    private fun Request.getActionConverterMapOrThrow(): Map<String, (JsonObject) -> ContextAction> {
        val actionConverterMap = contextActionConverterMap[context]

        if (actionConverterMap == null) {
            val availableContexts = contextActionConverterMap.keys

            val response = createResponse(
                reaction = Reaction.ContextNotFound,
                data = Json.encodeToJsonElement(
                    ErrorData(
                        action = action,
                        message = "Available contexts: $availableContexts"
                    )
                ).jsonObject
            )

            throw ConversionException(response = response)
        }

        return actionConverterMap
    }

    private fun Request.convertToActionOrThrow(
        converterMap: Map<String, (JsonObject) -> ContextAction>
    ): ContextAction {
        val converter = converterMap[action]

        if (converter == null) {
            val availableActions = converterMap.keys

            val response = createResponse(
                reaction = Reaction.ActionNotFound,
                data = Json.encodeToJsonElement(
                    ErrorData(
                        action = action,
                        message = "Available for context is: $availableActions"
                    )
                ).jsonObject
            )

            throw ConversionException(response = response)
        }

        return try {
            converter(data)
        } catch (exception: SerializationException) {
            val response = createResponse(
                reaction = Reaction.DataStructureError,
                data = Json.encodeToJsonElement(
                    ErrorData(
                        action = action,
                        message = exception.message
                    )
                ).jsonObject
            )

            throw ConversionException(response = response)
        }
    }
}
