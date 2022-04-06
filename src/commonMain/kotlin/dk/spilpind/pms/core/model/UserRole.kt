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

    sealed class ContextRole(context: RawContext, role: RawRole) {
        val context = context.identifier
        val role = role.identifier

        sealed class Team(role: Roles) : ContextRole(context = RawContext.Team, role = role) {
            enum class Roles(override val identifier: String) : RawRole {
                Captain("captain")
            }

            object Captain : Team(role = Roles.Captain)
        }
    }

    data class Raw(
        override val userRoleId: Int,
        override val userId: Int,
        override val context: String,
        override val contextId: Int,
        override val role: String,
        override val isPublic: Boolean
    ) : UserRole

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

        fun findContext(contextIdentifier: String, roleIdentifier: String): ContextRole {
            val rawContext = RawContext.values().firstOrNull { context ->
                context.identifier == contextIdentifier
            }

            return when (rawContext) {
                RawContext.Team -> when (
                    ContextRole.Team.Roles.values().findRoleOrNull(identifier = roleIdentifier)
                ) {
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

        private fun <T> Array<T>.findRoleOrNull(identifier: String): T? where T : Enum<T>, T : RawRole {
            return firstOrNull { role -> role.identifier == identifier }
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
