package me.user.application.security
import org.mindrot.jbcrypt.BCrypt

fun hashPassword(password: String): String{
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun isPasswordCorrect(passwordToCheck: String, hashedPassword: String): Boolean{
    return BCrypt.checkpw(passwordToCheck, hashedPassword)
}