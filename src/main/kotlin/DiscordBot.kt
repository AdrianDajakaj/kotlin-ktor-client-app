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
                    message.content.startsWith("!products") -> handleProductsCommand(message.content)

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
                    append("- ${it.name}\n")
                    append("\tDescription: ${it.description}\n")
                }
            }
            
            message.channel.createMessage(responseText)
        } catch (e: Exception) {
            message.channel.createMessage("Failed to fetch category. Error: ${e.message}")
        }
    }

    private suspend fun MessageCreateEvent.handleProductsCommand(command: String) {
        try {
            val categoryName = command.removePrefix("!products").trim()
            val categories = getCategories()
            val category = categories.find { it.name.equals(categoryName, true) }
            if(category == null){
                message.channel.createMessage("Category: '$categoryName' does not exist!")
            }
            else{
                val products = getProducts(category.id)
                val responseText = buildString {
                    append("**Products from category ${category.name}:**\n")
                    products.forEach {
                        append("- ${it.name}\n")
                        append("\tDescription: ${it.description}\n")
                        append("\tStock: ${it.stock}\n")
                        append("\tPrice: ${it.price} ${it.currency}\n")
                        if (it.discount != null) append("\tDiscount: ${it.discount} %\n")
                    }
                }
                message.channel.createMessage(responseText)
            }
        } catch (e: Exception) {
            message.channel.createMessage("Failed to fetch products. Error: ${e.message}")
        }
    }

    private suspend fun getCategories(): List<Category> {
        val response = httpClient.get("$apiBaseUrl/categories")
        return json.decodeFromString(response.bodyAsText())
    }

    private suspend fun getProducts(categoryId: Long): List<Product> {
        val response = httpClient.get("$apiBaseUrl/products")
        val allProducts = json.decodeFromString<List<Product>>(response.bodyAsText())
        return allProducts.filter { it.categoryId == categoryId }
    }

}