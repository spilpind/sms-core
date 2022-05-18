package dk.spilpind.pms.api.common

/**
 * All possible actions that can be made across all contexts in the api. An action is a request for something to be
 * done, e.g. [Add] is a request to add an item and [Subscribe] is a request to start listening for changes to one or
 * a group of items. Note that each action might mean something different in various contexts and not all are relevant
 * for all contexts, so make sure to check out the specific actions for each context
 */
enum class Action(val actionKey: String) {
    Inform("inform"),
    Add("add"),
    Remove("remove"),
    Update("update"),
    Accept("accept"),
    Subscribe("subscribe"),
    Unsubscribe("unsubscribe"),
}
