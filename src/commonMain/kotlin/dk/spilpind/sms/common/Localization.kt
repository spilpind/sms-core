package dk.spilpind.sms.common

/**
 * Localizations of core components
 */
interface Localization {
    val unknownErrorPermanent: String

    /**
     * Danish localizations
     */
    object Danish : Localization {
        override val unknownErrorPermanent: String =
            "Der skete en ukendt fejl. Tjek om du har nyeste version af appen og prøv igen."
    }
}
