package dk.spilpind.pms.api.data

import kotlinx.serialization.Serializable

@Serializable
data class ErrorData(val action: String?, val message: String?)
