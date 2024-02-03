package dev.joewilliams.easycasts.model

data class Episode(
    val id: Long,
    val title: String,
    val description: String,
    val durationSeconds: Int
)
