package dk.spilpind.pms.core.model

/**
 * Represents data about a user of the system. This can e.g. be an admin or a referee
 */
data class User(val userId: Int, val name: String, val username: String, val email: String, val accessLevel: Int)
