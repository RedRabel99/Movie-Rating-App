package models

import kotlinx.serialization.Serializable

@Serializable
data class PublicUser(
    val id: Int,
    val username: String,
    val createdAt: String
)
