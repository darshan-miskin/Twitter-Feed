package com.gne.twitter.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gne.twitter.retrofit.response.tweets.Tweet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Tweet::class], version = 1, exportSchema = false)
abstract class TweetDatabase : RoomDatabase() {
    abstract fun daoInterface(): TweetDao

    private class TweetDatabaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    //
                }
            }
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: TweetDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope): TweetDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TweetDatabase::class.java,
                        "db_tweets"
                ).addCallback(TweetDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}