package dk.spilpind.sms.core.model

import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.api.common.Reaction

/**
 * All possible contexts. A context represents a group of functionality that usually is related directly to some data.
 *
 * Context is for instance used for the API, where there is a set of [Action]s and [Reaction]s for each context. In that
 * way it's possible to distinguish between if the user of the API e.g. wants to add a game or a team
 */
enum class Context(val contextKey: String) {
    Authentication("authentication"),
    User("user"),
    UserRole("userRole"),
    Tournament("tournament"),
    Game("game"),
    Team("team"),
    Referee("referee"),
}
