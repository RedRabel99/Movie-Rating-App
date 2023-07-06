
import kotlinx.serialization.Serializable

@Serializable
data class SyncResponse<T>(
    val data: T?=null,
    val statusCode: StatusCode,
    val message: String?
)

@Serializable
data class StatusCode(val value: Int, val description: String)
