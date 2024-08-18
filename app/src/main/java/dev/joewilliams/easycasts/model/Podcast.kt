package dev.joewilliams.easycasts.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Podcast(
    @PrimaryKey(autoGenerate = false)
    @SerialName("collectionId")
    val id: Long = 0,
    @SerialName("collectionName")
    val name: String = "",
    @SerialName("feedUrl")
    val sourceUri: String? = null,
    @SerialName("artworkUrl60")
    val smallImgUrl: String? = null,
    @SerialName("artworkUrl600")
    val largeImgUrl: String? = null,
    @SerialName("trackName")
    val description: String = ""
) {
    fun isIncomplete(): Boolean {
        return id == 0L
                || name.isEmpty()
                || sourceUri.isNullOrEmpty()
                || smallImgUrl.isNullOrEmpty()
                || largeImgUrl.isNullOrEmpty()
                || description.isEmpty()
    }
}

@Serializable
data class ItunesResponse(
    val resultCount: Int,
    val results: List<Podcast>
)