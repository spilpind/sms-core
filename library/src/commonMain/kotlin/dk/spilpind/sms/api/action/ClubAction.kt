package dk.spilpind.sms.api.action

import dk.spilpind.sms.api.common.Action
import dk.spilpind.sms.core.model.Context
import kotlinx.serialization.Serializable

/**
 * All possible actions that can be made in relation to [Context.Club]
 */
@Serializable
sealed class ClubAction : ContextAction() {
    override val context: Context = Context.Club

    /**
     * Adds a new club. A successful response to this would be [ClubReaction.Added]
     */
    @Serializable
    data class Add(
        val name: String,
        val location: String,
        val logoUrl: String,
    ) : ClubAction() {
        override val action: Action = Action.Add
    }

    /**
     * Removes the club with id [clubId]. The club must not be associated with any teams or alike. A successful response
     * to this would be [ClubReaction.Removed]
     */
    @Serializable
    data class Remove(val clubId: Int) : ClubAction() {
        override val action: Action = Action.Remove
    }

    /**
     * Updates the club with id [clubId] with the values provided. It is important to consider all values even though
     * you're not allowed to change them, as for instance a value set to empty or null will be updated to that value
     */
    @Serializable
    data class Update(
        val clubId: Int,
        val name: String,
        val location: String,
        val logoUrl: String,
    ) : ClubAction() {
        override val action: Action = Action.Update
    }

    /**
     * Subscribes either to all clubs or the single club associated with [clubId] if specified. The subscription will be
     * kept alive until the session is destroyed or [Unsubscribe] is called. Changes to the list will be sent via
     * relevant [ClubReaction]s. A successful response to this would be [ClubReaction.Subscribed] followed by
     * [ClubReaction.Updated]
     */
    @Serializable
    data class Subscribe(val clubId: Int?) : ClubAction() {
        override val action: Action = Action.Subscribe
    }

    /**
     * Unsubscribes the socket from updates to the list of clubs, previously subscribed by [Subscribe]. A successful
     * response to this would be [ClubReaction.Unsubscribed]
     */
    @Serializable
    data class Unsubscribe(val clubId: Int?) : ClubAction() {
        override val action: Action = Action.Unsubscribe
    }
}
