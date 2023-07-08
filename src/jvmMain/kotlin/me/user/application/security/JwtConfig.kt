package me.user.application.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtConfig private constructor(secret: String){

    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    fun createAccessToken(id: Int): String = JWT
        .create()
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withClaim(CLAIM, id)
        .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60000))
        .sign(algorithm)

    companion object{
        private const val ISSUER = "movie-rating-app"
        private  const val AUDIENCE = "movie-rating-app"
        const val CLAIM = "id"

        lateinit var instance: JwtConfig
            private set

        fun initialize(secret: String){
            synchronized(this){
                if(!this::instance.isInitialized) instance = JwtConfig(secret)
            }
        }
    }
}