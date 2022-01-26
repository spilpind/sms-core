package dk.spilpind.pms.api.common

/**
 * All possible contexts of the api requests and responses. A context represents a group of functionality that usually
 * is related directly to some data. All contexts have a set of [Action]s and [Reaction]s, see specific enum classes for
 * each context. Actions and reactions are grouped into these contexts, so it's possible to distinguish between if the
 * user of the api e.g. wants to add a game or a team to the system
 */
enum class Context(val contextKey: String) {
    Authentication("authentication"),
    User("user"),
    Tournament("tournament"),
    Game("game"),
    Team("team"),
    Referee("referee"),
}
