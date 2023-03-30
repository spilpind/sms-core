package dk.spilpind.sms.core.model

/**
 * Represents data about a user. Note that [googleEmail] and [appleEmail] might be null in some cases, e.g. if the user
 * signed up with the opposite account provider or if the logged in user doesn't have access to see the email of that
 * particular user
 */
sealed interface User {
    val userId: Int
    val name: String
    val googleEmail: String?
    val appleEmail: String?

    /**
     * Represents a raw user
     */
    data class Raw(
        override val userId: Int,
        override val name: String,
        override val googleEmail: String?,
        override val appleEmail: String?
    ) : User

    /**
     * Represents a user with privileges (see [roles]). Note that the list of user roles might be empty if the user
     * doesn't have any - in that case the user would just have default privileges as a logged-in user
     */
    data class Privileged(
        override val userId: Int,
        override val name: String,
        override val googleEmail: String?,
        override val appleEmail: String?,
        val roles: List<UserRole.Simple>
    ) : User
}
