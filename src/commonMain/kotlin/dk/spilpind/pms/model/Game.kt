package dk.spilpind.pms.model

data class Game(
    val gameId: Int?,
    val tournament: Tournament,
    val teamA: Team,
    val teamB: Team,
    val description: String,
    val focus: Boolean
)
