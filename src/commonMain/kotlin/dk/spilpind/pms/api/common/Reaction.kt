package dk.spilpind.pms.api.common

/**
 * All possible reactions that can be made across all contexts in the api. A reaction is usually a direct response to an
 * action (like [Added] or [Fetched]), but can also represent a reoccurring event if the user e.g. subscribed to
 * something ([Updated] could be a such reoccurring reaction). Not all are relevant for all contexts, so make sure to
 * check out the specific reactions for each context
 */
enum class Reaction(val reactionKey: String) {
    Informed("informed"),
    Added("added"),
    Removed("removed"),
    Updated("updated"),
    Fetched("fetched"),
    Accepted("accepted"),
    Subscribed("subscribed"),
    Unsubscribed("unsubscribed"),
    ServerError("serverError"),
    EncodingError("encodingError"),
    ContextNotFound("contextNotFound"),
    ActionNotFound("actionNotFound"),
    DataStructureError("dataStructureError"),
    DataValueError("dataValueError"),
    UnsafeOperation("unsafeOperation"),
    InvalidJsonWebToken("invalidJsonWebToken"),
    MissingPermission("missingPermission"),
    ItemNotFound("itemNotFound"),
}
