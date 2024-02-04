package dev.joewilliams.easycasts.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Podcast(
    @SerialName("collectionId")
    val id: Long?,
    @SerialName("collectionName")
    val name: String,
    @SerialName("feedUrl")
    val sourceUri: String? = null,
    @SerialName("artworkUrl60")
    val smallImgUrl: String?,
    @SerialName("artworkUrl600")
    val largeImgUrl: String?,
    @SerialName("trackName")
    val description: String
)

@Serializable
data class ItunesResponse(
    val resultCount: Int,
    val results: List<Podcast>
)