package com.github.adriandajakaj

import io.ktor.server.application.*
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSockets()
    configureFrameworks()
    configureMonitoring()
    configureSerialization()
    configureSecurity()
    configureHTTP()
    configureRouting()
    val discordBotToken = System.getenv("DISCORD_BOT_TOKEN")
    launch {
        DiscordBot(discordBotToken).start()
    }

}
