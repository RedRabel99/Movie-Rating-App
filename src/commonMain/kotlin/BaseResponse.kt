
import io.ktor.http.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonSerialize
@Serializable
sealed class BaseResponse<T>(
    @Contextual open val statusCode: HttpStatusCode
) {
    data class SuccessResponse<T>(
        val data: T? = null,
        val message: String? = null,
        @Contextual
        override val statusCode: HttpStatusCode = HttpStatusCode.OK
    ): BaseResponse<T>(statusCode)
    data class ErrorResponse(
        val message: String,
        @Contextual
        override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
    ) : BaseResponse<Any>(statusCode)
}

