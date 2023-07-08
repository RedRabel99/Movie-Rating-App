package params

import kotlinx.serialization.Serializable

@Serializable
data class CreateLoginParams(val email: String, val password: String)