package dk.spilpind.sms.core.model

/**
 * Represents a user's role in a specific context, like a game, team or tournament. [contextId] represents the id in the
 * context - for instance, if context is "game" it is expected to be the id of the game this invite relates to
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
        // TODO: Use context from api instead
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
                    null -> throwNotFound<ContextRole.Team.Roles>(
                        rawContext = rawContext,
                        roleIdentifier = roleIdentifier
                    )
                }
                null -> throw IllegalArgumentException(
                    "Context \"$contextIdentifier\" not found. Available contexts: "
                            + "${RawContext.values().map { availableContext -> availableContext.identifier }}"
                )
            }
        }

        private inline fun <reified RoleType> findRoleOrNull(identifier: String): RoleType?
                where RoleType : Enum<RoleType>, RoleType : RawRole {
            return enumValues<RoleType>().firstOrNull { role -> role.identifier == identifier }
        }

        private inline fun <reified RoleType> throwNotFound(
            rawContext: RawContext,
            roleIdentifier: String
        ): Nothing where RoleType : Enum<RoleType>, RoleType : RawRole {
            val availableRoles = enumValues<RoleType>()

            throw IllegalArgumentException(
                "Role \"$roleIdentifier\" not found in context \"${rawContext.identifier}\". Available roles: "
                        + "${availableRoles.map { availableRole -> availableRole.identifier }}"
            )
        }
    }
}
