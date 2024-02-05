package dev.joewilliams.easycasts.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.joewilliams.easycasts.model.Episode
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {
    @Query("SELECT * FROM Episode")
    fun allEpisodes(): Flow<List<Episode>>

    @Query("SELECT * FROM Episode WHERE id IS (:id)")
    fun episodesForPodcast(id: Long?): Flow<List<Episode>>

    @Query("SELECT * FROM Episode WHERE id IS (:id)")
    fun episodeForPodcastWithGuid(id: Long?): Episode

    @Insert
    suspend fun insertEpisode(episode: Episode): Long?

    @Insert
    suspend fun insertEpisodes(vararg episodes: Episode)

    @Update
    suspend fun updateEpisode(episode: Episode)
}