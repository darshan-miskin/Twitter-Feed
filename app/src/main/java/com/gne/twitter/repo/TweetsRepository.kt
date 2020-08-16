package com.gne.twitter.repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.gne.twitter.custom.Status
import com.gne.twitter.isNetworkAvailable
import com.gne.twitter.retrofit.ApiClient
import com.gne.twitter.retrofit.ApiInterface
import com.gne.twitter.retrofit.response.tweets.Tweet
import com.gne.twitter.retrofit.response.tweets.TweetsResponse
import com.gne.twitter.room.RoomRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TweetsRepository(private val context: Context, private val viewModelScope: CoroutineScope) {
    private val apiInterface: ApiInterface = ApiClient.getInstance()
    private val listMutableLiveData = MutableLiveData<TweetsResponse>()
    private val roomRepo: RoomRepo = RoomRepo(context, viewModelScope)

    fun getListMutableLiveData(userId: String): MutableLiveData<TweetsResponse> {
        if (isNetworkAvailable(context)) {
            roomRepo.deleteTweets()
            val call = apiInterface.getTweets(userId)
            call.enqueue(object : Callback<List<Tweet>> {
                override fun onResponse(call: Call<List<Tweet>>, response: Response<List<Tweet>>) {
                    if (response.isSuccessful && response.body() != null) {
                        val list:ArrayList<Tweet> = response.body() as ArrayList<Tweet>
                        roomRepo.insertTweets(list)
                        listMutableLiveData.setValue(TweetsResponse(Status.SUCCESS, list, ""))
                    } else {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        val errorMessage=jsonObj.getJSONArray("errors").getJSONObject(0).getString("message")
                        listMutableLiveData.setValue(TweetsResponse(Status.ERROR, null, errorMessage))
                    }
                }

                override fun onFailure(call: Call<List<Tweet>>, t: Throwable) {
                    listMutableLiveData.setValue(TweetsResponse(Status.FAILURE, null, t.toString()))
                }
            })
        } else {
            viewModelScope.launch(Dispatchers.IO){
                val list:List<Tweet> = roomRepo.getTweets()
                listMutableLiveData.postValue(TweetsResponse(Status.SUCCESS, list, ""))
            }
        }
        return listMutableLiveData
    }

    fun getListMutableLiveData(userId: String, lastTweetId: String): MutableLiveData<TweetsResponse> {
        if (isNetworkAvailable(context)) {
            val call = apiInterface.getTweetsSince(lastTweetId, userId)
            call.enqueue(object : Callback<List<Tweet>> {
                override fun onResponse(call: Call<List<Tweet>>, response: Response<List<Tweet>>) {
                    if (response.isSuccessful && response.body() != null) {
                        val list = response.body() as MutableList<Tweet>
                        list.removeAt(0)
                        roomRepo.insertTweets(list)
                        listMutableLiveData.setValue(TweetsResponse(Status.SUCCESS, list, ""))
                    } else {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        val errorMessage=jsonObj.getJSONArray("errors").getJSONObject(0).getString("message")
                        listMutableLiveData.setValue(TweetsResponse(Status.ERROR, null, errorMessage))
                    }
                }

                override fun onFailure(call: Call<List<Tweet>>, t: Throwable) {
                    listMutableLiveData.setValue(TweetsResponse(Status.FAILURE, null, t.toString()))
                }
            })
        } else {
            viewModelScope.launch(Dispatchers.IO){
                val list=roomRepo.getLastTweets(lastTweetId.toLong())
                listMutableLiveData.postValue(TweetsResponse(Status.SUCCESS, list, ""))
            }
        }
        return listMutableLiveData
    }
}