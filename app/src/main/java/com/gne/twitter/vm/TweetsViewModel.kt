package com.gne.twitter.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.gne.twitter.repo.TweetsRepository
import com.gne.twitter.retrofit.response.tweets.Tweet
import com.gne.twitter.retrofit.response.tweets.TweetsResponse
import com.twitter.sdk.android.core.TwitterCore
import java.util.ArrayList

class TweetsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TweetsRepository
    private var userId:String

    var listLiveData: LiveData<TweetsResponse>
    val tweetList: MutableList<Tweet> = ArrayList()

    init {
        repository = TweetsRepository(application, viewModelScope)

        val session = TwitterCore.getInstance().sessionManager.activeSession
        userId  = session.userId.toString()
        listLiveData=repository.getListMutableLiveData(userId)
    }

    fun addTweets(newTweets: List<Tweet>){
        this.tweetList.addAll(newTweets)
    }

    fun getLastTweet():Tweet{
        return tweetList.get(tweetList.lastIndex)
    }

    fun getListLiveDataSince() {
        repository.getListMutableLiveData(userId, getLastTweet().idStr)
    }

}