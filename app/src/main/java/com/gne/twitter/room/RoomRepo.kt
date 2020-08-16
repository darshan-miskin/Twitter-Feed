package com.gne.twitter.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.gne.twitter.retrofit.response.tweets.Tweet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomRepo(val context: Context, val viewModelScope: CoroutineScope) {
    private val tweetDao: TweetDao = TweetDatabase.getDatabase(context, viewModelScope).daoInterface()

    fun deleteTweets() = viewModelScope.launch(Dispatchers.IO) {
        tweetDao.deleteAll()
    }

    fun insertTweets(tweets: List<Tweet>) = viewModelScope.launch(Dispatchers.IO) {
        tweetDao.insertAllTweets(tweets)
    }

    suspend fun getTweets(): List<Tweet> {
        return tweetDao.fetchAllTweets()
    }

    suspend fun getLastTweets(lastId: Long): List<Tweet> {
        return tweetDao.fetchLastTweets(lastId)
    }

}