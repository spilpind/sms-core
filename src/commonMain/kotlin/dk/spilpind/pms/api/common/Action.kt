package dk.spilpind.pms.api.common

/**
 * All possible actions that can be made across all contexts in the api. An action is a request for something to be
 * done, e.g. [Add] is a request to add an item and [Fetch] is a request to fetch one or all items. Not all are relevant
 * for all contexts, so make sure to check out the specific actions for each context
 */
enum class Action(val actionKey: String) {
    Inform("inform"),
    Add("add"),
    Remove("remove"),
    Update("update"),
    Fetch("fetch"),
    Accept("accept"),
    Subscribe("subscribe"),
    Unsubscribe("unsubscribe"),
}
