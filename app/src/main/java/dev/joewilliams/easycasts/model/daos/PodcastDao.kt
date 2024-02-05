package dev.joewilliams.easycasts.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.joewilliams.easycasts.model.Podcast
import dev.joewilliams.easycasts.model.PodcastWithEpisodes
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {

    @Query("SELECT * FROM Podcast")
    fun allPodcasts(): Flow<List<Podcast>>

    @Transaction
    @Query("SELECT * FROM Podcast WHERE id IS (:id) LIMIT 1")
    fun getPodcast(id: Long?): Flow<PodcastWithEpisodes>

    @Query("SELECT * FROM Podcast WHERE id IS (:id) LIMIT 1")
    fun getPodcastByItunesId(id: Long?): Podcast?

    @Query("SELECT * FROM Podcast WHERE sourceUri IS (:sourceUri) LIMIT 1")
    fun getPodcastByFeed(sourceUri: String): Podcast?

    @Transaction
    @Query("SELECT * FROM Podcast WHERE id IS (:id) LIMIT 1")
    fun getPodcastWithEpisodes(id: Long?): PodcastWithEpisodes?

    @Query("SELECT * FROM Podcast WHERE sourceUri IS (:sourceUri) LIMIT 1")
    fun getPodcastWithEpisodes(sourceUri: String): PodcastWithEpisodes?

    @Insert
    suspend fun insertPodcast(podcast: Podcast): Long?

    @Update
    suspend fun updatePodcast(podcast: Podcast)
}