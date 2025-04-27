package com.github.adriandajakaj

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

val webhookUrl = System.getenv("DISCORD_WEBHOOK_URL")

suspend fun sendDiscordMessage(message: String) {
    val client = HttpClient(CIO)
    try {
        val response: HttpResponse = client.post(webhookUrl) {
            contentType(ContentType.Application.Json)
            setBody(
                """{
                    "content": "$message"
                }"""
            )
        }
        if (response.status == HttpStatusCode.NoContent) {
            println("Message sent on discord!")
        } else {
            println("Error sending message to Discord: ${response.status}")
        }
    } catch (e: Exception) {
        println("Error sending message: ${e.message}")
    } finally {
        client.close()
    }
}

