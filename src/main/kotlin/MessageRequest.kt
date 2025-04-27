package com.github.adriandajakaj

import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(
    val content: String
)
