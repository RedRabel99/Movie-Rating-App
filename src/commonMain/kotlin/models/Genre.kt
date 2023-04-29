package models

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Int,
    val name: String
){
    companion object{
        const val path = "/genres"
    }
}
