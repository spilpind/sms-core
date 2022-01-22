package dk.spilpind.pms.core.model

/**
 * Represents overall game data
 */
data class Game(
    val gameId: Int?,
    val tournament: Tournament,
    val teamA: Team?,
    val teamB: Team?,
    val description: String,
    val isFocused: Boolean
)
