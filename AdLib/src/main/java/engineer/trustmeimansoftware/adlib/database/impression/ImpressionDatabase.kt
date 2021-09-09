package engineer.trustmeimansoftware.adlib.database.impression

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Models Room Database for Impressions
 * @property impressionDao
 */
@Database(entities = [ImpressionDbItem::class], version = 1)
abstract class ImpressionDatabase: RoomDatabase() {
    abstract val impressionDao: ImpressionDao
}

private lateinit var INSTANCE: ImpressionDatabase

/**
 * returns database if initialized
 * inits database if not initialized
 */
fun getDatabase(context: Context): ImpressionDatabase {
    synchronized(context) {
        if(!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    ImpressionDatabase::class.java,
                    "impression")
                .build()
        }
    }

    return INSTANCE
}

/**
 * returns database instance
 * <p><b>DOES NOT INITIALIZE DATABASE IF IT DOES NOT EXIST ALREADY</b></p>
 */
fun getDatabase(): ImpressionDatabase {
    return INSTANCE
}