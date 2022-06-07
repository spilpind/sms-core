package dk.spilpind.sms.core.model

/**
 * Represents data about a user. Note that [email] might be null in some cases, e.g. if the logged in user doesn't have
 * access to see the email of that particular user
 */
sealed interface User {
    val userId: Int
    val name: String
    val email: String?

    /**
     * Represents a raw user
     */
    data class Raw(
        override val userId: Int,
        override val name: String,
        override val email: String?
    ) : User

    /**
     * Represents a user with privileges (see [roles]). Note that the list of user roles might be empty if the user
     * doesn't have any - in that case the user would just have default in-logged privileges
     */
    data class Privileged(
        override val userId: Int,
        override val name: String,
        override val email: String?,
        val roles: List<UserRole.Simple>
    ) : User
}
