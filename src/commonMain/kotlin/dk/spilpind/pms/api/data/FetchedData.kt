package dk.spilpind.pms.api.data

/**
 * Generic interface used with responses to fetch actions, where it isn't a specific item
 */
interface FetchedData<DataType> {
    val items: List<DataType>
}
