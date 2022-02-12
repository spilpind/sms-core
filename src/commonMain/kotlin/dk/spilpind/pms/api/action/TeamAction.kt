package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Action
import dk.spilpind.pms.api.common.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.Team]
 */
@Serializable
sealed class TeamAction : ContextAction() {
    override val context: Context = Context.Team

    /**
     * Adds a new team. A successful response to this would be [TeamReaction.Added]
     */
    @Serializable
    data class Add(val name: String, val shortName: String, val tournamentId: Int) : TeamAction() {
        override val action: Action = Action.Add
        override val minimumAccessLevel: Int = 2
    }

    /**
     * Removes the team with id [teamId]. The team cannot be used for any games or alike. A successful response to this
     * would be [TeamReaction.Removed]
     */
    @Serializable
    data class Remove(val teamId: Int) : TeamAction() {
        override val action: Action = Action.Remove
        override val minimumAccessLevel: Int = 2
    }

    /**
     * Fetches the team with id [teamId] or all teams if it's null. A successful response to this would be
     * [TeamReaction.Fetched]
     */
    @Serializable
    data class Fetch(val teamId: Int? = null) : TeamAction() {
        override val action: Action = Action.Fetch
        override val minimumAccessLevel: Int? = null
    }
}
