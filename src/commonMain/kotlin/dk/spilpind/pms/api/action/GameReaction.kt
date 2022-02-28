package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction
import dk.spilpind.pms.api.data.FetchedData
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.Game]
 */
@Serializable
sealed class GameReaction : ContextReaction() {

    /**
     * Response to [GameAction.Add]
     */
    @Serializable
    data class Added(
        val gameId: Int,
        val description: String,
        val focused: Boolean
    ) : GameReaction() {
        override val reaction: Reaction = Reaction.Added
    }

    /**
     * Response to [GameAction.Remove]
     */
    @Serializable
    data class Removed(val gameId: Int) : GameReaction() {
        override val reaction: Reaction = Reaction.Removed
    }

    /**
     * Response to [GameAction.Fetch]. If the request specified a specific game id, [items] will contain that game as
     * the only item
     */
    @Serializable
    data class Fetched(override val items: List<Game>) : GameReaction(), FetchedData<Game> {
        override val reaction: Reaction = Reaction.Fetched
    }

    /**
     * Represents a single game
     */
    @Serializable
    data class Game(val gameId: Int, val description: String, val focused: Boolean)
}
