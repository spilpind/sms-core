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
     * Generic error in case of an unknown error. This is usually not an error the client can fix and is hopefully only
     * temporary - if the client did something wrong it will instead be represented by one of the other errors
     */
    @Serializable
    data class ServerError(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.ServerError
    }

    /**
     * The structure of the request was malformed and could not be deserialized. This could e.g. be a missing property
     * or an actual malformed structure. If structure could be deserialized but values were incorrect, other errors will
     * be used instead. This is a client error
     */
    @Serializable
    data class RequestStructureError(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.RequestStructureError
    }

    /**
     * The request type could not be determined based on the provided context and action. This is a client error
     */
    @Serializable
    data class RequestTypeError(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.RequestTypeError
    }

    /**
     * The value(s) of the data was insufficient, wrong or invalid. This can e.g. be because a value isn't within a
     * valid range. This is a client error, but can usually be fixed by the user correcting the wrong value(s) - the
     * error can therefore be treated more as a warning to the user
     */
    @Serializable
    data class DataValueError(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.DataValueError
    }

    /**
     * The request would result in an undefined state. This can happen e.g. if some data is requested to be deleted by
     * referred to by some other data - in that case the other data should be deleted first. This is a client error, but
     * can be treated more as a warning to the user
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
     * user needs to log in, but can e.g. also be because the user doesn't have permission to alter or view the specific
     * data. This is a client error, but could potentially be fixed by a re-authorization
     */
    @Serializable
    data class MissingPermission(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.MissingPermission
    }

    /**
     * The requested item was not found in the system. This is used e.g. if a specific item was requested or referenced
     * and could not be found. Requesting all games for a tournament would result in this error if the tournament was
     * not found, but not if there just weren't any games - in that case an empty list of games will be the result. This
     * is a client error
     */
    @Serializable
    data class ItemNotFound(
        override val localizedMessage: String,
        override val debugMessage: String?
    ) : ErrorReaction() {
        override val reaction: Reaction = Reaction.ItemNotFound
    }
}
