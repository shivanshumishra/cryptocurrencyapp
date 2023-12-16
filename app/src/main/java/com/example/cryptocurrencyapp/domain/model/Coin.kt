package com.example.cryptocurrencyapp.domain.model

import com.google.gson.annotations.SerializedName

data class Coin(
    val id: String,
    @SerializedName("is_active")
    val isActive: Boolean,
    val name: String,
    val rank: Int,
    val symbol: String,
) {
    fun doesMatchSearchQuery(query: String) : Boolean {
        val matchingCombination = listOf(name, "${name.first()}")

        return matchingCombination.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
