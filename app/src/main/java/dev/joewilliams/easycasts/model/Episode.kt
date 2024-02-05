package dev.joewilliams.easycasts.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Episode(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val podcastId: Long,
    val title: String,
    val description: String,
    val durationSeconds: Int
)
