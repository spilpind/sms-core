package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction
import dk.spilpind.pms.api.data.FetchedData
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.Tournament]
 */
@Serializable
sealed class TournamentReaction : ContextReaction() {
    override val context: Context = Context.Tournament

    /**
     * Response to [TournamentAction.Add]
     */
    @Serializable
    data class Added(val tournament: Tournament) : TournamentReaction() {
        override val reaction: Reaction = Reaction.Added
    }

    /**
     * Response to [TournamentAction.Remove]
     */
    @Serializable
    data class Removed(val tournamentId: Int) : TournamentReaction() {
        override val reaction: Reaction = Reaction.Removed
    }

    /**
     * Response to [TournamentAction.Fetch]. If the request specified a specific tournament id, [items] will contain
     * that tournament as the only item
     */
    @Serializable
    data class Fetched(
        override val items: List<Tournament>
    ) : TournamentReaction(), FetchedData<Tournament> {
        override val reaction: Reaction = Reaction.Fetched
    }

    /**
     * Response to changes in one or more of the tournaments
     */
    @Serializable
    data class Updated(
        val allItems: Boolean,
        val items: List<Tournament>
    ) : TournamentReaction() {
        override val reaction: Reaction = Reaction.Updated
    }

    /**
     * Response to [TournamentAction.Subscribe]
     */
    @Serializable
    class Subscribed : TournamentReaction() {
        override val reaction: Reaction = Reaction.Subscribed
    }

    /**
     * Response to [TournamentAction.Unsubscribe]
     */
    @Serializable
    class Unsubscribed : TournamentReaction() {
        override val reaction: Reaction = Reaction.Unsubscribed
    }

    /**
     * Represents a single tournament
     */
    @Serializable
    data class Tournament(val tournamentId: Int, val name: String, val isPublic: Boolean, val tags: List<String>)
}
