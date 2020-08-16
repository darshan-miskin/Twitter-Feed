package com.gne.twitter.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gne.twitter.retrofit.response.tweets.Tweet

@Dao
interface TweetDao {
    @Query("DELETE FROM Tweet")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTweets(tweet: List<Tweet>)

    @Query("SELECT * FROM Tweet ORDER BY id DESC LIMIT 20")
    suspend fun fetchAllTweets(): List<Tweet>

    @Query("SELECT * FROM Tweet where id<:lastId ORDER BY id DESC LIMIT 20")
    suspend fun fetchLastTweets(lastId: Long): List<Tweet>
}