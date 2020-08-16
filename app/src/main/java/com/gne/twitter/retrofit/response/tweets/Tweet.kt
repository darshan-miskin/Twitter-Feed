package com.gne.twitter.retrofit.response.tweets

import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.facebook.drawee.view.SimpleDraweeView
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity
data class Tweet(@PrimaryKey
                 @SerializedName("id")
                 @Expose
                 var id: Long,

                 @SerializedName("id_str")
                 @Expose
                 val idStr: String,

                 @SerializedName("text")
                 @Expose
                 val text: String,

                 @Embedded
                 @SerializedName("user")
                 @Expose
                 val user: User,

                 @SerializedName("retweet_count")
                 @Expose
                 val retweetCount: Long,

                 @SerializedName("favorite_count")
                 @Expose
                 val favoriteCount: Long) : Serializable, BaseObservable() {

}