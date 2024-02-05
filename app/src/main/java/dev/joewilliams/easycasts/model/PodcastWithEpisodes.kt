package dev.joewilliams.easycasts.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
open class PodcastWithEpisodes(
    @Embedded
    val podcast: Podcast,

    @Relation(
        parentColumn = "id",
        entityColumn = "podcastId"
    )
    val episodes: List<Episode>
)