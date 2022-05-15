package dk.spilpind.pms.api.action

import dk.spilpind.pms.api.common.Context
import dk.spilpind.pms.api.common.Reaction
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
     * Response to changes in one or more of the tournaments. If [allTournaments] is false, only the listed tournaments
     * should be updated - if it's true, the entire list should be replaced
     */
    @Serializable
    data class Updated(
        val allTournaments: Boolean,
        val tournaments: List<Tournament>
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
