package dk.spilpind.sms.api.core

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Represents current status of the service running the API. [localizedMessage] can, if provided, can be shown to the
 * user to indicate what the state is and what they can expect of the service
 */
@Serializable
data class Status(
    val type: Type,
    val localizedMessage: String?
) {
    @Serializable(with = TypeSerializer::class)
    enum class Type {
        /**
         * The service is fully functional and open for everyone
         */
        Open,

        /**
         * The service is fully functional, but restricted and thus cannot be used without special access. This is
         * expected to be temporary, but can be for a longer period of time.
         *
         * This can for instance be during games of a tournament where referees must have access, but the servers has
         * experienced too much load and thus can't allow non-essential clients to access the service
         */
        Restricted,

        /**
         * The service is under maintenance (being upgraded, fixed or alike). This is expected to be temporary for a
         * short period of time
         */
        Maintenance,

        /**
         * The service is in an (unexpected) error state and is not expected to become online again until someone
         * manually fixes the issue
         */
        Error,

        /**
         * The way of fetching the status was outdated and the client should be updated to match the newest version of
         * the api
         */
        Outdated,

        /**
         * The state was not possible to deduct as it was unknown to the current state of the core components. This type
         * should never be used directly as a response from the api
         */
        Unknown,
    }

    private object TypeSerializer : KSerializer<Type> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
            serialName = "SmsCore.Status.Type",
            kind = PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: Type) {
            val typeName = value.name.lowercase()
            encoder.encodeString(typeName)
        }

        override fun deserialize(decoder: Decoder): Type {
            val typeName = decoder.decodeString()
            return Type.entries.find { type ->
                type.name.lowercase() == typeName
            } ?: Type.Unknown
        }
    }
}
