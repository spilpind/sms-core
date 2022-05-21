package dk.spilpind.pms.core.model

/**
 * Represents a user's role in a specific context, like a game, team or tournament
 */
sealed interface UserRole {
    val userRoleId: Int
    val userId: Int
    val context: String
    val contextId: Int
    val role: String
    val isPublic: Boolean

    /**
     * Defines all types of user roles that exists
     */
    sealed class ContextRole(context: RawContext, role: RawRole) {
        val context = context.identifier
        val role = role.identifier

        /**
         * Defines all types of user roles that exists for a team
         */
        sealed class Team(role: Roles) : ContextRole(context = RawContext.Team, role = role) {
            enum class Roles(override val identifier: String) : RawRole {
                Captain("captain")
            }

            /**
             * A user role representing a captain of a team
             */
            object Captain : Team(role = Roles.Captain)
        }
    }

    /**
     * Represents the raw user role
     */
    data class Raw(
        override val userRoleId: Int,
        override val userId: Int,
        override val context: String,
        override val contextId: Int,
        override val role: String,
        override val isPublic: Boolean
    ) : UserRole

    /**
     * Like [Raw], but with a specified [contextRole]
     */
    data class Simple(
        override val userRoleId: Int,
        override val userId: Int,
        val contextRole: ContextRole,
        override val contextId: Int,
        override val isPublic: Boolean
    ) : UserRole {

        override val context: String = contextRole.context

        override val role: String = contextRole.role

    }

    companion object {
        internal enum class RawContext(val identifier: String) {
            Team("team")
        }

        internal interface RawRole {
            val identifier: String
        }

        /**
         * Finds a [ContextRole] based on the provided parameters or throw an [IllegalArgumentException] if not found
         */
        fun findContext(contextIdentifier: String, roleIdentifier: String): ContextRole {
            val rawContext = RawContext.values().firstOrNull { context ->
                context.identifier == contextIdentifier
            }

            return when (rawContext) {
                RawContext.Team -> when (findRoleOrNull<ContextRole.Team.Roles>(identifier = roleIdentifier)) {
                    ContextRole.Team.Roles.Captain -> ContextRole.Team.Captain
                    null -> throwArgumentException(
                        rawContext = rawContext,
                        roleIdentifier = roleIdentifier,
                        availableRoles = ContextRole.Team.Roles.values()
                    )
                }
                null -> throw IllegalArgumentException(
                    "Context \"$contextIdentifier\" not found. Available contexts: "
                            + "${RawContext.values().map { availableContext -> availableContext.identifier }}"
                )
            }
        }

        private inline fun <reified T> findRoleOrNull(identifier: String): T? where T : Enum<T>, T : RawRole {
            return enumValues<T>().firstOrNull { role -> role.identifier == identifier }
        }

        private fun <T, R> throwArgumentException(
            rawContext: RawContext,
            roleIdentifier: String,
            availableRoles: Array<T>
        ): R where T : Enum<T>, T : RawRole {
            throw IllegalArgumentException(
                "Role \"$roleIdentifier\" not found en context \"${rawContext.identifier}\". Available roles: "
                        + "${availableRoles.map { availableRole -> availableRole.identifier }}"
            )
        }
    }
}
