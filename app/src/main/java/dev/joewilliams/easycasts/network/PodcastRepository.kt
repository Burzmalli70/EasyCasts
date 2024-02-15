package dev.joewilliams.easycasts.network

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.joewilliams.easycasts.App
import dev.joewilliams.easycasts.model.Podcast
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class PodcastRepository {

    private val database = App.sharedInstance.database

    private val podcastApi by lazy {
        setupItunesApi()
    }

    private val converter = Json { ignoreUnknownKeys = true }

    suspend fun getPodcasts(): List<Podcast> {
        return database.podcastDao().allPodcasts()
    }

    val subscribedPodcasts = database.podcastDao().allPodcastsFlow()

    private fun setupItunesApi(): ItunesApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(ITUNES_URL)
            .addConverterFactory(converter.asConverterFactory("application/json".toMediaType()))
            .build()
        return retrofit.create(ItunesApi::class.java)
    }

    suspend fun searchPodcasts(query: String?): List<Podcast> {
        return try {
            val result = podcastApi.searchPodcasts(query ?: "")
            if (result.isSuccessful) {
                result.body()?.results ?: emptyList()
            } else {
                emptyList()
            }
        } catch(ex: Exception) {
            Log.e("Pod Search", "Search failed: ${ex.cause}")
            emptyList()
        }
    }

    suspend fun addPodcast(podcast: Podcast) {
        database.podcastDao().insertPodcast(podcast)
    }

    suspend fun removePodcast(podcast: Podcast) {
        podcast.sourceUri?.let { uri ->
            database.podcastDao().deleteByUri(uri)
        }
    }


    companion object {
        const val ITUNES_URL = "https://itunes.apple.com"
    }
}