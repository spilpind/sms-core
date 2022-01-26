package dk.spilpind.pms.api.data

import kotlinx.serialization.Serializable

/**
 * Generic data class used with responses to fetch actions, where it isn't a specific item
 */
@Serializable
data class FetchedData<DataType>(val items: List<DataType>)
