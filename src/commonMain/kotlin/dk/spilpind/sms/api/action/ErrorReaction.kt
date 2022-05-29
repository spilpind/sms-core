package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Reaction
import kotlinx.serialization.Serializable

/**
 * Error class used as reaction to all non-successful responses, as defined by the subclasses.
 */
@Serializable
sealed class ErrorReaction : ReactionData() {
    abstract val localizedMessage: String
    abstract val debugMessage: String?

    /**
     * Generic error in case an unknown error
     */
    @Serializable
    data class ServerError(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.ServerError
    }

    /**
     * The structure of the request was malformed and could not be deserialized. This could e.g. be
     * a missing property or actual malformed structure
     */
    @Serializable
    data class EncodingError(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.EncodingError
    }

    /**
     * The specified context was not found
     */
    @Serializable
    data class ContextNotFound(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.ContextNotFound
    }

    /**
     * The specified action was not found for the specified context
     */
    @Serializable
    data class ActionNotFound(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.ActionNotFound
    }

    /**
     * There was an error in the structure of the data part of the request. This could e.g. be a
     * missing property or actual malformed structure
     */
    @Serializable
    data class DataStructureError(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.DataStructureError
    }

    /**
     * The value(s) of the data was insufficient, wrong or invalid. This can e.g. be because a value
     * isn't within a valid range
     */
    @Serializable
    data class DataValueError(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.DataValueError
    }

    /**
     * The request would result in an undefined state. This can happen e.g. if some data is
     * requested to be deleted by referred to by some other data - in that case the other data
     * should be deleted first
     */
    @Serializable
    data class UnsafeOperation(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.UnsafeOperation
    }

    /**
     * The logged-in user (or lack of) does not have permissions to perform the action. This can be as simple as the
     * user needs to log in, but can also be because the user does not have permission to the action at all or because
     * the user doesn't have permission to alter or view the specific data
     */
    @Serializable
    data class MissingPermission(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.MissingPermission
    }

    /**
     * The requested item was not found in the system. E.g. for authorization this means the user
     * wasn't found in the database
     */
    @Serializable
    data class ItemNotFound(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.ItemNotFound
    }
}
