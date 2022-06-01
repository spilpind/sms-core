package dk.spilpind.sms.core.model

/**
 * Represents a user's role in a specific context, like a game, team or tournament. [contextId] represents the id in the
 * context - for instance, if context is "game" it is expected to be the id of the game this user role relates to
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
    sealed class ContextRole(context: RoleContext, role: RawRole) {
        val context = context.context
        val role = role.identifier

        /**
         * Defines all types of user roles that exists for the overall system
         */
        sealed class System(role: Roles) : ContextRole(context = RoleContext.System, role = role) {
            enum class Roles(override val identifier: String) : RawRole {
                SuperAdmin("superAdmin"),
                Admin("admin")
            }

            /**
             * A user role representing a super admin of the system, having at least the same rights as an [Admin]
             */
            object SuperAdmin : System(role = Roles.SuperAdmin)

            /**
             * A user role representing an admin of the system
             */
            object Admin : System(role = Roles.Admin)
        }

        /**
         * Defines all types of user roles that exists for a team
         */
        sealed class Team(role: Roles) : ContextRole(context = RoleContext.Team, role = role) {
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

        override val context: String = contextRole.context.contextKey

        override val role: String = contextRole.role

    }

    companion object {
        internal enum class RoleContext(val context: Context) {
            System(Context.System),
            Team(Context.Team)
        }

        internal interface RawRole {
            val identifier: String
        }

        /**
         * Finds a [ContextRole] based on the provided parameters or throw an [IllegalArgumentException] if not found
         */
        fun findContext(contextIdentifier: String, roleIdentifier: String): ContextRole {
            val context = RoleContext.values().firstOrNull { context ->
                context.context.contextKey == contextIdentifier
            }

            return when (context) {
                RoleContext.System -> when (findRoleOrNull<ContextRole.System.Roles>(identifier = roleIdentifier)) {
                    ContextRole.System.Roles.SuperAdmin -> ContextRole.System.SuperAdmin
                    ContextRole.System.Roles.Admin -> ContextRole.System.Admin
                    null -> throwNotFound<ContextRole.Team.Roles>(
                        context = context,
                        roleIdentifier = roleIdentifier
                    )
                }
                RoleContext.Team -> when (findRoleOrNull<ContextRole.Team.Roles>(identifier = roleIdentifier)) {
                    ContextRole.Team.Roles.Captain -> ContextRole.Team.Captain
                    null -> throwNotFound<ContextRole.Team.Roles>(
                        context = context,
                        roleIdentifier = roleIdentifier
                    )
                }
                null -> throw IllegalArgumentException(
                    "Context \"$contextIdentifier\" not found. Available contexts: "
                            + "${RoleContext.values().map { availableContext -> availableContext.context.contextKey }}"
                )
            }
        }

        private inline fun <reified RoleType> findRoleOrNull(identifier: String): RoleType?
                where RoleType : Enum<RoleType>, RoleType : RawRole {
            return enumValues<RoleType>().firstOrNull { role -> role.identifier == identifier }
        }

        private inline fun <reified RoleType> throwNotFound(
            context: RoleContext,
            roleIdentifier: String
        ): Nothing where RoleType : Enum<RoleType>, RoleType : RawRole {
            val availableRoles = enumValues<RoleType>()

            throw IllegalArgumentException(
                "Role \"$roleIdentifier\" not found in context \"${context.context.contextKey}\". Available roles: "
                        + "${availableRoles.map { availableRole -> availableRole.identifier }}"
            )
        }
    }
}
