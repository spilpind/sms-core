package dk.spilpind.sms.core.model

import kotlin.jvm.JvmInline

/**
 * Represents a user's role in a specific context, like a game, team or tournament. [contextId] represents the id in the
 * context - for instance, if context is "game" it is expected to be the id of the game this user role relates to
 */
sealed interface UserRole {
    val userRoleId: Id
    val userId: User.Id
    val context: String
    val contextId: Int
    val role: String
    val isPublic: Boolean

    /**
     * Id of a user. Can be used to reference a user without having to care about the remaining user data
     */
    @JvmInline
    value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

        override fun compareTo(other: Id): Int =
            identifier.compareTo(other.identifier)

    }

    /**
     * Defines all types of user roles that exists
     */
    sealed class ContextRole(context: RoleContext, role: RawRole) {
        val context = context.context
        val role = role.identifier

        /**
         * Defines all types of user roles that exists for the overall system
         */
        sealed class System(role: Role) : ContextRole(context = RoleContext.System, role = role) {
            enum class Role(override val identifier: String) : RawRole {
                SuperAdmin("superAdmin"),
                Admin("admin")
            }

            /**
             * A user role representing a super admin of the system, having at least the same rights as an [Admin]
             */
            object SuperAdmin : System(role = Role.SuperAdmin)

            /**
             * A user role representing an admin of the system
             */
            object Admin : System(role = Role.Admin)
        }

        /**
         * Defines all types of user roles that exists for a game
         */
        sealed class Game(role: Role) : ContextRole(context = RoleContext.Game, role = role) {
            enum class Role(override val identifier: String) : RawRole {
                Referee("referee"),
            }

            /**
             * A user role representing a referee of a game
             */
            object Referee : Game(role = Role.Referee)
        }

        /**
         * Defines all types of user roles that exists for a team
         */
        sealed class Team(role: Role) : ContextRole(context = RoleContext.Team, role = role) {
            enum class Role(override val identifier: String) : RawRole {
                Captain("captain"),
                Member("member")
            }

            /**
             * A user role representing a captain of a team
             */
            object Captain : Team(role = Role.Captain)

            /**
             * A user role representing a member of a team
             */
            object Member : Team(role = Role.Member)
        }
    }

    /**
     * Represents the raw user role
     */
    data class Raw(
        override val userRoleId: Id,
        override val userId: User.Id,
        override val context: String,
        override val contextId: Int,
        override val role: String,
        override val isPublic: Boolean
    ) : UserRole

    /**
     * Like [Raw], but with a specified [contextRole]
     */
    data class Simple(
        override val userRoleId: Id,
        override val userId: User.Id,
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
            Game(Context.Game),
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
                RoleContext.System -> when (
                    findRoleOrThrow<ContextRole.System.Role>(context = context, roleIdentifier = roleIdentifier)
                ) {
                    ContextRole.System.Role.SuperAdmin -> ContextRole.System.SuperAdmin
                    ContextRole.System.Role.Admin -> ContextRole.System.Admin
                }
                RoleContext.Game -> when (
                    findRoleOrThrow<ContextRole.Game.Role>(context = context, roleIdentifier = roleIdentifier)
                ) {
                    ContextRole.Game.Role.Referee -> ContextRole.Game.Referee
                }
                RoleContext.Team -> when (
                    findRoleOrThrow<ContextRole.Team.Role>(context = context, roleIdentifier = roleIdentifier)
                ) {
                    ContextRole.Team.Role.Captain -> ContextRole.Team.Captain
                    ContextRole.Team.Role.Member -> ContextRole.Team.Member
                }
                null -> throw IllegalArgumentException(
                    "Context \"$contextIdentifier\" not found. Available contexts: "
                            + "${RoleContext.values().map { availableContext -> availableContext.context.contextKey }}"
                )
            }
        }

        private inline fun <reified RoleType> findRoleOrThrow(
            context: RoleContext,
            roleIdentifier: String
        ): RoleType where RoleType : Enum<RoleType>, RoleType : RawRole {
            val availableRoles = enumValues<RoleType>()

            return availableRoles.firstOrNull { role ->
                role.identifier == roleIdentifier
            } ?: throw IllegalArgumentException(
                "Role \"$roleIdentifier\" not found in context \"${context.context.contextKey}\". Available roles: "
                        + "${availableRoles.map { availableRole -> availableRole.identifier }}"
            )
        }
    }
}
