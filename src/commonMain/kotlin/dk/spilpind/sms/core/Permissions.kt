package dk.spilpind.sms.core

import dk.spilpind.sms.api.action.TeamAction
import dk.spilpind.sms.core.model.*
import dk.spilpind.sms.core.model.ModelHelper.isCurrentStickLeague

/**
 * Defines functionality to determine if the logged-in user (or "everyone" if null) is allowed to perform a specific
 * action. It's important to notice that this is only privilege-wise, not considering e.g. business logic like adding
 * duplicates of user roles or using an already used name for a team
 */
object Permissions {

    /**
     * Checks if the user has the specified [systemRole] or a superior one
     */
    fun User.Privileged.hasSystemRole(systemRole: UserRole.ContextRole.System): Boolean {
        return when (systemRole) {
            UserRole.ContextRole.System.SuperAdmin -> roles.any { role ->
                role.contextRole == systemRole
            }
            UserRole.ContextRole.System.Admin -> roles.any { role ->
                role.contextRole == systemRole || role.contextRole == UserRole.ContextRole.System.SuperAdmin
            }
        }
    }

    /**
     * Checks if the user has an exact match of the specified combination of [role] and [contextId]
     */
    fun User.Privileged.hasRole(role: UserRole.ContextRole, contextId: ContextIdentifier): Boolean {
        return roles.any { actualRole ->
            actualRole.contextRole == role
                    && actualRole.contextId == contextId.identifier
        }
    }

    /**
     * Checks if the user can view the specified [user] or any user if [user] is null
     */
    fun User.Privileged.canViewUser(user: User?): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.Admin) || (user != null && userId == user.userId)
    }

    /**
     * Checks if the user can remove any user
     */
    fun User.Privileged.canRemoveUsers(): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.SuperAdmin)
    }

    /**
     * Checks if the user can view any user role of the specified [user]
     */
    fun User.Privileged.canViewUserRole(user: User): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.Admin) || userId == user.userId
    }

    /**
     * Checks if the user can add the specified [role]. If the user role is for a specific context id, it should be
     * specified via [contextId] or else it's a general check for any context id. Note that this mainly is meant to be
     * used when directly/only adding a user role, if it's added as part of some other action (like for instance when
     * [TeamAction.Add.addAsCaptain] is set to true) other rules might apply
     */
    fun User.Privileged.canAddUserRole(role: UserRole.ContextRole, contextId: ContextIdentifier?): Boolean {
        return when (role) {
            UserRole.ContextRole.System.SuperAdmin,
            UserRole.ContextRole.System.Admin -> hasSystemRole(UserRole.ContextRole.System.SuperAdmin)
            UserRole.ContextRole.Team.Captain -> hasSystemRole(UserRole.ContextRole.System.Admin)
                    || (contextId != null && hasRole(UserRole.ContextRole.Team.Captain, contextId = contextId))
            UserRole.ContextRole.Team.Member -> hasSystemRole(UserRole.ContextRole.System.Admin)
                    || (contextId != null && hasRole(UserRole.ContextRole.Team.Captain, contextId = contextId))
        }
    }

    /**
     * Checks if the user (or "everyone" if null) can view the specified [tournament]
     */
    fun User.Privileged?.canViewTournament(tournament: Tournament): Boolean {
        return tournament.isPublic || (this != null && hasSystemRole(UserRole.ContextRole.System.Admin))
    }

    /**
     * Checks if the user can add any kind of tournament
     */
    fun User.Privileged.canAddTournaments(): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.Admin)
    }

    /**
     * Checks if the user can remove any tournament
     */
    fun User.Privileged.canRemoveTournaments(): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.Admin)
    }

    /**
     * Checks if the user (or "everyone" if null) can view the specified [game] when associated with the specified
     * [tournament]
     */
    fun User.Privileged?.canViewGame(game: Game, tournament: Tournament): Boolean {
        return if (this != null && hasSystemRole(UserRole.ContextRole.System.Admin)) {
            true
        } else if (!canViewTournament(tournament = tournament)) {
            false
        } else if (tournament.isCurrentStickLeague) {
            canViewStickLeagueGame(game)
        } else {
            true
        }
    }

    /**
     * Checks if the user (or "everyone" if null) can view the specified [game]
     */
    fun User.Privileged?.canViewGame(game: Game.Detailed): Boolean {
        return canViewGame(game = game, tournament = game.tournament)
    }

    /**
     * Checks if the user can add any kind of game
     */
    fun User.Privileged.canAddGames(): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.Admin)
    }

    /**
     * Checks if the user can add a game with the specified [tournament] and set of [teams]
     */
    fun User.Privileged.canAddGame(tournament: Tournament, teams: List<Team>): Boolean {
        return if (canAddGames()) {
            true
        } else if (tournament.isCurrentStickLeague && teams.size == 1) {
            hasRole(role = UserRole.ContextRole.Team.Captain, contextId = teams.first().teamId)
        } else {
            false
        }
    }

    /**
     * Checks if the user can remove any game
     */
    fun User.Privileged.canRemoveGames(): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.Admin)
    }

    /**
     * Checks if the user can create an invite such that another team can join the [game]
     */
    fun User.Privileged.canCreateTeamJoinInviteForGame(game: Game): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.Admin) ||
                game.teamAId?.let { teamId ->
                    hasRole(role = UserRole.ContextRole.Team.Captain, contextId = teamId)
                } ?: false ||
                game.teamBId?.let { teamId ->
                    hasRole(role = UserRole.ContextRole.Team.Captain, contextId = teamId)
                } ?: false
    }

    /**
     * Checks if the user can add the specified [team] to a game on behalf of the team
     */
    fun User.Privileged.canJoinGame(team: Team): Boolean {
        return hasRole(role = UserRole.ContextRole.Team.Captain, contextId = team.teamId)
    }

    /**
     * Checks if the user (or "everyone" if null) can view teams with the specified [tournament] or any team if
     * [tournament] is null
     */
    fun User.Privileged?.canViewTeam(tournament: Tournament?): Boolean {
        return if (this != null && hasSystemRole(UserRole.ContextRole.System.Admin)) {
            true
        } else {
            tournament != null && canViewTournament(tournament)
        }
    }

    /**
     * Checks if the user can add any kind of team
     */
    fun User.Privileged.canAddTeams(): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.Admin)
    }


    /**
     * Checks if the user can add teams with the specified [tournament]
     */
    fun User.Privileged.canAddTeam(tournament: Tournament): Boolean {
        return canAddTeams() || tournament.isCurrentStickLeague
    }

    /**
     * Checks if the user can remove any team
     */
    fun User.Privileged.canRemoveTeams(): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.Admin)
    }

    /**
     * Checks if the user can invite other users to become members of the [team]
     */
    fun User.Privileged.canInviteMembers(team: Team): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.Admin)
                || hasRole(role = UserRole.ContextRole.Team.Captain, contextId = team.teamId)
    }

    fun User.Privileged.canRevokeMemberInvite(team: Team): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.Admin)
                || hasRole(role = UserRole.ContextRole.Team.Captain, contextId = team.teamId)
    }

    /**
     * Checks if the user can judge (and thus be a referee of) [game]
     */
    fun User.Privileged.canJudgeGame(game: Game.Detailed): Boolean {
        return if (hasSystemRole(UserRole.ContextRole.System.Admin)) {
            true
        } else if (game.tournament.isCurrentStickLeague) {
            val teamId = game.teamAId ?: return false // This is the team creating the game
            hasRole(role = UserRole.ContextRole.Team.Captain, contextId = teamId)
        } else {
            false
        }
    }

    /**
     * Checks if the user can ignore prompts during the game, like when it's time to switch teams or end the game
     */
    fun User.Privileged.canIgnorePrompts(): Boolean {
        return hasSystemRole(UserRole.ContextRole.System.Admin)
    }

    private fun User.Privileged?.canViewStickLeagueGame(game: Game): Boolean {
        val teamAId = game.teamAId
        val teamBId = game.teamBId
        return if (teamAId != null && teamBId != null) {
            true
        } else if (this == null) {
            false
        } else if (teamAId != null) {
            hasRole(role = UserRole.ContextRole.Team.Captain, contextId = teamAId)
        } else if (teamBId != null) {
            hasRole(role = UserRole.ContextRole.Team.Captain, contextId = teamBId)
        } else {
            false
        }
    }
}
