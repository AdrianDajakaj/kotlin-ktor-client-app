package com.github.adriandajakaj

import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.Instant

@Serializable
data class Category(
    val id: Long,
    val name: String,
    val description: String
)

@Serializable
data class Product(
    val id: Long,
    val name: String,
    val categoryId: Long,
    val description: String,
    val stock: Int,
    val price: Double,
    val currency: String,
    val discount: Double? = null,
    val createdAt: String,
    val updatedAt: String
)

