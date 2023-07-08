package params

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserParams(
    val username: String,
    val email: String,
    val password: String
)