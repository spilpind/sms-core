package dk.spilpind.sms.core.model

import kotlin.jvm.JvmInline

/**
 * Represents data about a club. A club groups teams under a shared identity and is described by a [name], a
 * [shortName], a [location] (free-text, e.g. a city) and a [logoUrl] pointing at the club's logo
 */
data class Club(
    val clubId: Id,
    val name: String,
    val shortName: String,
    val location: String,
    val logoUrl: String,
) {

    /**
     * Id of a club. Can be used to reference a club without having to care about the remaining club data
     */
    @JvmInline
    value class Id(override val identifier: Int) : ContextIdentifier, Comparable<Id> {

        override fun compareTo(other: Id): Int =
            identifier.compareTo(other.identifier)

    }
}
