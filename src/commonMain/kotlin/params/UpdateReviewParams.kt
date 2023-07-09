package params

import kotlinx.serialization.Serializable

@Serializable
data class UpdateReviewParams(
    var id: Int? = null,
    val rating: Int?,
    val review: String?,
    var userId: Int? = null
)