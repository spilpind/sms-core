package dk.spilpind.sms.core.model

import kotlin.jvm.JvmInline

/**
 * Represents data about a tournament
 */
data class Tournament(val tournamentId: Id, val name: String, val isPublic: Boolean, val tags: List<String>) {

    /**
     * Id of a tournament. Can be used to reference a tournament without having to care about the tournament game data
     */
    @JvmInline
    value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

        override fun compareTo(other: Id): Int =
            identifier.compareTo(other.identifier)

    }
}
