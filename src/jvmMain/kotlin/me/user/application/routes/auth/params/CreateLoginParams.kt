package me.user.application.routes.auth.params

import kotlinx.serialization.Serializable

@Serializable
data class CreateLoginParams(val email: String, val password: String)