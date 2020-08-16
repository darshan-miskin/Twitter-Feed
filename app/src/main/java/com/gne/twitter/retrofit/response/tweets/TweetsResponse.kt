package com.gne.twitter.retrofit.response.tweets

import com.gne.twitter.custom.Status

data class TweetsResponse(val status:Status, val tweetsList: List<Tweet>?, val errorMessage:String="")