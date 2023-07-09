package params
import kotlinx.serialization.Serializable

@Serializable
data class CreateReviewParams(
    val movieId: Int,
    var userId: Int? = null,
    val rating: Int,
    val review: String
)