package dev.joewilliams.easycasts.network

import android.util.Log
import android.util.Xml
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.joewilliams.easycasts.App
import dev.joewilliams.easycasts.model.Episode
import dev.joewilliams.easycasts.model.Podcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.xmlpull.v1.XmlPullParser
import retrofit2.Retrofit
import java.net.URL

class PodcastRepository {

    private val database = App.sharedInstance.database

    private val itunesApi by lazy {
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
            if (query?.startsWith(URL_PREFIX) == true) {
                parseUrlForPodcast(query)?.let {
                    listOf(it)
                } ?: emptyList()
            } else {
                val result = itunesApi.searchITunes(query ?: "")

                if (result.isSuccessful) {
                    result.body()?.results ?: emptyList()
                } else {
                    emptyList()
                }
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

    suspend fun getPodcastEpisodes(podcast: Podcast): List<Episode>? {
        return withContext(Dispatchers.IO) {
            podcast.sourceUri?.let {
                val parser = Xml.newPullParser()
                val url = URL(it)
                val iStream = url.openStream()
                parser.setInput(iStream, null)
                parser.toEpisodes()
            }
        }
    }

    suspend fun parseUrlForPodcast(urlStr: String?): Podcast? {
        return withContext(Dispatchers.IO) {
             urlStr?.let {
                val parser = Xml.newPullParser()
                val url = URL(it)
                val iStream = url.openStream()
                parser.setInput(iStream, null)
                parser.toPodcast()
            }
        }
    }

    companion object {
        const val ITUNES_URL = "https://itunes.apple.com"
        const val URL_PREFIX = "http"
    }
}

private fun XmlPullParser.toPodcast(): Podcast? {
    var podcast = Podcast()
    var parsingEpisodes = false
    val episodes = mutableListOf<Episode>()
    var currentEpisode: Episode? = null
    while (eventType != XmlPullParser.END_DOCUMENT) {
        when(eventType) {
            XmlPullParser.START_TAG -> {
                if (parsingEpisodes) {
                    if (name == ITEM_TAG) {
                        currentEpisode = Episode()
                    } else {
                        when(name) {
                            TITLE_TAG -> currentEpisode = currentEpisode?.copy(title = nextText())
                            LINK_TAG -> currentEpisode = currentEpisode?.copy(sourceUri = nextText())
                            DATE_TAG -> currentEpisode = currentEpisode?.copy(publishedDate = nextText())
                            DESCRIPTION_TAG -> currentEpisode = currentEpisode?.copy(description = nextText())
                        }
                    }
                } else {
                    when (name) {
                        ITEM_TAG -> parsingEpisodes = true
                        TITLE_TAG -> podcast = podcast.copy(name = nextText())
                        DESCRIPTION_TAG -> podcast = podcast.copy(description = nextText())
                        IMG_TAG -> {
                            do {
                                next()
                            } while(name != URL_TAG && name != IMG_TAG)
                            if (name == URL_TAG) {
                                podcast = podcast.copy(
                                    largeImgUrl = nextText()
                                )
                            }
                        }
                    }
                }
            }
            XmlPullParser.END_TAG -> {
                when(name) {
                    ITEM_TAG -> {
                        currentEpisode?.let {
                            episodes.add(it)
                        }
                    }
                }
            }
        }
        next()
    }

    return if (podcast.name.isEmpty()) null else podcast
}

private fun XmlPullParser.toEpisodes(): List<Episode>? {
    var podcast = Podcast()
    var parsingEpisodes = false
    val episodes = mutableListOf<Episode>()
    var currentEpisode: Episode? = null
    while (eventType != XmlPullParser.END_DOCUMENT) {
        when(eventType) {
            XmlPullParser.START_TAG -> {
                if (parsingEpisodes) {
                    if (name == ITEM_TAG) {
                        currentEpisode = Episode()
                    } else {
                        when(name) {
                            TITLE_TAG -> currentEpisode = currentEpisode?.copy(title = nextText())
                            LINK_TAG -> currentEpisode = currentEpisode?.copy(sourceUri = nextText())
                            DATE_TAG -> currentEpisode = currentEpisode?.copy(publishedDate = nextText())
                            DESCRIPTION_TAG -> currentEpisode = currentEpisode?.copy(description = nextText())
                        }
                    }
                } else {
                    when (name) {
                        ITEM_TAG -> parsingEpisodes = true
                        TITLE_TAG -> podcast = podcast.copy(name = nextText())
                        DESCRIPTION_TAG -> podcast = podcast.copy(description = nextText())
                        IMG_TAG -> {
                            do {
                                next()
                            } while(name != URL_TAG && name != IMG_TAG)
                            if (name == URL_TAG) {
                                podcast = podcast.copy(
                                    largeImgUrl = nextText()
                                )
                            }
                        }
                    }
                }
            }
            XmlPullParser.END_TAG -> {
                when(name) {
                    ITEM_TAG -> {
                        currentEpisode?.let {
                            episodes.add(it)
                        }
                    }
                }
            }
        }
        next()
    }

    return if (podcast.name.isEmpty()) null else episodes
}

const val TITLE_TAG = "title"
const val DESCRIPTION_TAG = "description"
const val IMG_TAG = "image"
const val URL_TAG = "url"
const val ITEM_TAG = "item"
const val LINK_TAG = "link"
const val DATE_TAG = "pubDate"
