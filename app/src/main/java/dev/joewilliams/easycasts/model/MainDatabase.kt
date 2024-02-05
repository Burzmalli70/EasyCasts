package dev.joewilliams.easycasts.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.joewilliams.easycasts.model.daos.EpisodeDao
import dev.joewilliams.easycasts.model.daos.PodcastDao

@Database(
    entities = [Podcast::class, Episode::class],
    version = 1,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {

    abstract fun podcastDao(): PodcastDao
    abstract fun episodeDao(): EpisodeDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java, "main_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}