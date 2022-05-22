package dk.spilpind.sms.api

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Various common functionality used by the serializers
 */
object SerializationUtil {

    /**
     * Returns the element as a string if it is a string - null otherwise
     */
    fun JsonElement.asStringOrNull(): String? {
        return if (jsonPrimitive.isString) {
            jsonPrimitive.content
        } else {
            null
        }
    }

    /**
     * If the data property exists, [edit] is called in order ot be able to alter it. The altered
     * element (including the edited data) will be returned
     */
    fun JsonElement.alterData(
        edit: MutableMap<String, JsonElement>.() -> Unit
    ): JsonElement {
        val data = jsonObject["data"]?.jsonObject?.toMutableMap()?.apply {
            edit()
        }

        return if (data == null) {
            this
        } else {
            JsonObject(jsonObject.toMutableMap().apply {
                put("data", JsonObject(data))
            })
        }
    }
}
