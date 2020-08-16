package com.gne.twitter.retrofit;

import com.gne.twitter.retrofit.response.tweets.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("/1.1/statuses/home_timeline.json")
    Call<List<Tweet>> getTweets(@Query("user_id") String userId);

    @GET("/1.1/statuses/home_timeline.json")
    Call<List<Tweet>> getTweetsCount(@Query("count") String count, @Query("user_id") String userId);

    @GET("/1.1/statuses/home_timeline.json")
    Call<List<Tweet>> getTweetsSince(@Query("max_id") String max_id, @Query("user_id") String userId);
}
