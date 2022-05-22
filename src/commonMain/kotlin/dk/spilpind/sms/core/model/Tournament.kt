package dk.spilpind.sms.core.model

/**
 * Represents data about a tournament
 */
data class Tournament(val tournamentId: Int, val name: String, val isPublic: Boolean, val tags: List<String>)
