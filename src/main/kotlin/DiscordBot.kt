package com.github.adriandajakaj

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import dev.kord.gateway.PrivilegedIntent
import dev.kord.gateway.Intent
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class DiscordBot(private val token: String) {
    private val apiBaseUrl = System.getenv("API_BASE_URL")
    private val httpClient = HttpClient(CIO)
    private val json = Json { ignoreUnknownKeys = true }
    fun start() = runBlocking {
        val client = Kord(token)
        client.on<MessageCreateEvent> {
            if (message.author?.isBot == false) {
                when{
                    message.content.startsWith("!categories") -> handleCategoriesCommand()
                }
                println("Received message from ${message.author?.username}: ${message.content}")
                try {
                    message.channel.createMessage("Thanks for the message, ${message.author?.username}!")
                    println("Reply sent to ${message.author?.username}")
                } catch (e: Exception) {
                    println("Error sending response: ${e.message}")
                }
            }
        }        

        client.login {
            @OptIn(PrivilegedIntent::class)
            intents += Intent.MessageContent
        }
    }

    private suspend fun MessageCreateEvent.handleCategoriesCommand() {
        try {
            val response = httpClient.get("$apiBaseUrl/categories")
            val categories = json.decodeFromString<List<Category>>(response.bodyAsText())
            
            val responseText = buildString {
                append("**Categories list:**\n")
                categories.forEach {
                    append("- ${it.name}\n Description: ${it.description}\n")
                }
            }
            
            message.channel.createMessage(responseText)
        } catch (e: Exception) {
            message.channel.createMessage("Failed to fetch category. Error: ${e.message}")
        }
    }

    private suspend fun getCategories(): List<Category> {
        val response = httpClient.get("$apiBaseUrl/categories") {
        headers {
            append("ngrok-skip-browser-warning", "true")
        }
    }
    return json.decodeFromString(response.bodyAsText())
    }

}