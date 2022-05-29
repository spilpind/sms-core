package dk.spilpind.sms.api

import kotlinx.serialization.json.*

/**
 * Various common functionality used by the serializers
 */
object SerializationUtil {

    private const val POLYMORPHIC_SERIALIZATION_TYPE_SERIAL_NAME = "type"

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
     * If the object by the key [key] exists, [edit] is called in order ot be able to alter it. The altered element
     * (including the edited data) will be returned
     */
    fun JsonElement.alterObjectIfExists(
        key: String,
        edit: MutableMap<String, JsonElement>.() -> Unit
    ): JsonElement {
        val jsonElement = jsonObject[key]?.jsonObject ?: return this

        val data = jsonElement.toMutableMap().apply {
            edit()
        }

        return JsonObject(jsonObject.toMutableMap().apply {
            put(key, JsonObject(data))
        })
    }

    /**
     * Sets the type property needed for polymorphic serialization
     */
    fun MutableMap<String, JsonElement>.putType(type: String) {
        put(POLYMORPHIC_SERIALIZATION_TYPE_SERIAL_NAME, JsonPrimitive(type))
    }

    /**
     * Removes the type property (used for polymorphic serialization), useful when it's not needed anymore
     */
    fun MutableMap<String, JsonElement>.removeType() {
        remove(POLYMORPHIC_SERIALIZATION_TYPE_SERIAL_NAME)
    }
}
