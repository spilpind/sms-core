package dk.spilpind.pms.api.common

enum class Action(val actionKey: String) {
    Inform("inform"),
    Add("add"),
    Remove("remove"),
    Update("update"),
    Fetch("fetch"),
    Subscribe("subscribe"),
    Unsubscribe("unsubscribe"),
}
