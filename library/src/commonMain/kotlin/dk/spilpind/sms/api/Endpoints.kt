package dk.spilpind.sms.api

import dk.spilpind.sms.api.common.Language

/**
 * The endpoints of the api and what they take along, so a client and a server can't end up disagreeing about where the
 * api is or what it is called
 */
object Endpoints {

    /**
     * Paths of the endpoints, relative to the host the api runs on
     */
    object Path {

        /**
         * Tells the current status of the service, which decides whether it makes sense to connect at all
         */
        const val STATUS = "/api/v1/status"

        /**
         * The websocket carrying all actions and reactions
         */
        const val STREAM = "/api/v1/stream"
    }

    /**
     * Names of the query parameters the endpoints understand
     */
    object Query {

        /**
         * Decides the language of the localized messages, set to [Language.languageKey] and understood by both
         * [Path.STATUS] and [Path.STREAM]
         *
         * It is deliberately not called "locale": a locale is normally a language plus a region and the formatting
         * rules that come with it (e.g. da_DK), whereas this is only the language, so calling it a locale would invite
         * clients to send something like en-GB and expect dates and numbers to follow
         */
        const val LANGUAGE = "language"
    }
}
