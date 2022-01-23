package dk.spilpind.pms.api.common

enum class Context(val contextKey: String) {
    Authentication("authentication"),
    User("user"),
    Tournament("tournament"),
    Game("game"),
    Team("team"),
    Referee("referee"),
}
