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
    override val context: Context = Context.Game

    /**
     * Response to [GameAction.Add]
     */
    @Serializable
    data class Added(
        val gameId: Int,
        val tournamentId: Int,
        val teamAId: Int?,
        val teamBId: Int?,
        val description: String,
        val isFocused: Boolean
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
     * Response to changes in one or more of the games
     */
    @Serializable
    data class Updated(
        val allItems: Boolean,
        val items: List<Game>
    ) : GameReaction() {
        override val reaction: Reaction = Reaction.Updated
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
     * Response to [GameAction.Accept]. [gameId] will represent the game which the invite was related to
     */
    @Serializable
    data class Accepted(val gameId: Int) : GameReaction() {
        override val reaction: Reaction = Reaction.Accepted
    }

    /**
     * Response to [GameAction.Subscribe]
     */
    @Serializable
    class Subscribed : GameReaction() {
        override val reaction: Reaction = Reaction.Subscribed
    }

    /**
     * Response to [GameAction.Unsubscribe]
     */
    @Serializable
    class Unsubscribed : GameReaction() {
        override val reaction: Reaction = Reaction.Unsubscribed
    }

    /**
     * Represents a single game
     */
    @Serializable
    data class Game(
        val gameId: Int,
        val tournamentId: Int,
        val teamAId: Int?,
        val teamBId: Int?,
        val description: String,
        val isFocused: Boolean
    )
}
