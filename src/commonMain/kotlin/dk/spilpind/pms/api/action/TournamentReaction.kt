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
    data class Added(val tournamentId: Int, val name: String) : TournamentReaction() {
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
     * Represents a single tournament
     */
    @Serializable
    data class Tournament(val tournamentId: Int, val name: String)
}
