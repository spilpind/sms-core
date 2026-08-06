package dk.spilpind.sms.api.common

/**
 * All languages the api can provide localized messages in. A client tells the api which one it wants when it connects,
 * using [languageKey] - see [dk.spilpind.sms.api.Endpoints] - and every localized message of that connection is then in
 * that language. Note that the api is expected to fall back to a default language if the client doesn't ask for one (or
 * asks for one the api doesn't know), as an unsupported language shouldn't keep a client from connecting
 */
enum class Language(val languageKey: String) {
    Danish("da"),
    English("en");

    companion object {

        /**
         * The language matching [languageKey], or null if no language matches it. Note that the match is case
         * insensitive as e.g. browsers aren't consistent about the casing of language tags
         */
        fun fromKey(languageKey: String): Language? = entries.find { language ->
            language.languageKey.equals(languageKey, ignoreCase = true)
        }
    }
}
