package com.github.adriandajakaj

import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import dev.kord.gateway.PrivilegedIntent
import dev.kord.gateway.Intent

class DiscordBot(private val token: String) {

    fun start() = runBlocking {
        val client = Kord(token)
        client.on<MessageCreateEvent> {
            if (message.author?.isBot == false) {
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
}