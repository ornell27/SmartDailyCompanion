package com.example.myapplication.data

object QuotesProvider {
    val quotes = listOf(
        "Believe you can and you're halfway there.",
        "Your only limit is your mind.",
        "Action is the foundational key to all success.",
        "Don’t stop until you’re proud.",
        "Difficult roads often lead to beautiful destinations.",
        "Make each day your masterpiece.",
        "Focus on being productive instead of busy."
    )

    fun getRandomQuote(): String = quotes.random()
}