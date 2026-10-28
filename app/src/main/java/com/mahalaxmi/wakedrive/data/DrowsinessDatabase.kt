package com.mahalaxmi.wakedrive.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mahalaxmi.wakedrive.data.models.DrowsinessEvent

@Database(
    entities = [DrowsinessEvent::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DrowsinessDatabase : RoomDatabase() {
    abstract fun drowsinessEventDao(): DrowsinessEventDao

    companion object {
        @Volatile
        private var INSTANCE: DrowsinessDatabase? = null

        fun getDatabase(context: Context): DrowsinessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrowsinessDatabase::class.java,
                    "drowsiness_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
