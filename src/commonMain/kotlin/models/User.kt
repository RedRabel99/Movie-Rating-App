package models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String,
    val email: String,
    var authToken: String? = null,
    val createdAt: String
) {
    companion object {
        const val path = "/auth"
    }
}