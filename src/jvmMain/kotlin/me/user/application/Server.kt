package me.user.application

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.user.application.config.configureContentNegotiation
import me.user.application.config.configureDatabase
import me.user.application.config.configureRouting
import me.user.application.security.configureSecurity


fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        configureDatabase()
        configureContentNegotiation()
        configureSecurity()
        configureRouting()
    }.start(wait = true)
}