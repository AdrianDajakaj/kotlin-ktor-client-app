package com.github.adriandajakaj

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

val webhookUrl = "https://discord.com/api/webhooks/1365932936809021551/pfNJX2gMBuGtoKGrj8Hunpem_grcF-acn5PAyc4jg5GiXiNhQKD43GvPMKDytlj8te8y"

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
        println("Wysłano wiadomość! Status: ${response.status}")
    } catch (e: Exception) {
        println("Błąd przy wysyłaniu wiadomości: ${e.message}")
    } finally {
        client.close()
    }
}
