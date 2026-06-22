package dk.spilpind.sms.api.action

import dk.spilpind.sms.core.model.Context
import dk.spilpind.sms.api.common.Reaction
import kotlinx.serialization.Serializable

/**
 * All possible reactions that can be made in relation to [Context.Club]
 */
@Serializable
sealed class ClubReaction : ContextReaction() {
    override val context: Context = Context.Club

    /**
     * Response to [ClubAction.Add]
     */
    @Serializable
    data class Added(val club: Club) : ClubReaction() {
        override val reaction: Reaction = Reaction.Added
    }

    /**
     * Response to [ClubAction.Remove]
     */
    @Serializable
    data class Removed(val clubId: Int) : ClubReaction() {
        override val reaction: Reaction = Reaction.Removed
    }

    /**
     * Response to changes in one or more of the clubs. If [allClubs] is false, only the listed clubs should be updated
     * - if it's true, the entire list should be replaced
     */
    @Serializable
    data class Updated(
        val allClubs: Boolean,
        val clubs: List<Club>
    ) : ClubReaction() {
        override val reaction: Reaction = Reaction.Updated
    }

    /**
     * Response to [ClubAction.Subscribe]
     */
    @Serializable
    class Subscribed : ClubReaction() {
        override val reaction: Reaction = Reaction.Subscribed
    }

    /**
     * Response to [ClubAction.Unsubscribe]
     */
    @Serializable
    class Unsubscribed : ClubReaction() {
        override val reaction: Reaction = Reaction.Unsubscribed
    }

    /**
     * Represents a single club
     */
    @Serializable
    data class Club(
        val clubId: Int,
        val name: String,
        val location: String,
        val logoUrl: String
    )
}
