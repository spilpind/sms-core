package dk.spilpind.pms.api.common

enum class Reaction(val reactionKey: String) {
    Informed("informed"),
    Added("added"),
    Removed("removed"),
    Updated("updated"),
    Fetched("fetched"),
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
