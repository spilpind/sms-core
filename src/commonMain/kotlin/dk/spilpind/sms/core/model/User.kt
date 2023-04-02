package dk.spilpind.sms.core.model

import kotlin.jvm.JvmInline

/**
 * Represents data about a user. Note that [googleEmail] and [appleEmail] might be null in some cases, e.g. if the user
 * signed up with the opposite account provider or if the logged in user doesn't have access to see the email of that
 * particular user
 */
sealed interface User {
    val userId: Id
    val name: String
    val googleEmail: String?
    val appleEmail: String?

    /**
     * Id of a user. Can be used to reference a user without having to care about the remaining user data
     */
    @JvmInline
    value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

        override fun compareTo(other: Id): Int =
            identifier.compareTo(other.identifier)

    }

    /**
     * Represents a raw user
     */
    data class Raw(
        override val userId: Id,
        override val name: String,
        override val googleEmail: String?,
        override val appleEmail: String?
    ) : User

    /**
     * Represents a user with privileges (see [roles]). Note that the list of user roles might be empty if the user
     * doesn't have any - in that case the user would just have default privileges as a logged-in user
     */
    data class Privileged(
        override val userId: Id,
        override val name: String,
        override val googleEmail: String?,
        override val appleEmail: String?,
        val roles: List<UserRole.Simple>
    ) : User
}
