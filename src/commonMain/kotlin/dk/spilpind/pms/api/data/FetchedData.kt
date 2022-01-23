package dk.spilpind.pms.api.data

import kotlinx.serialization.Serializable

@Serializable
data class FetchedData<DataType>(val items: List<DataType>)
