package dk.spilpind.pms.core.model

/**
 * Represents data about a team
 */
data class Team(val teamId: Int, val name: String, val shortName: String, val tournament: Tournament)
