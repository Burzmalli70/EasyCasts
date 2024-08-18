package dev.joewilliams.easycasts.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class Episode(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    val podcastId: Long = 0,
    val title: String = "",
    val description: String = "",
    val durationSeconds: Int = 0,
    val sourceUri: String? = null,
    val publishedDate: String? = null,
    val imgUri: String? = null
)
