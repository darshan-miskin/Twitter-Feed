package com.gne.twitter.retrofit.response.tweets

import androidx.room.ColumnInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(@ColumnInfo(name = "user_id")
                @SerializedName("id")
                @Expose
                val id: Long,

                @ColumnInfo(name = "user_strid")
                @SerializedName("id_str")
                @Expose
                val idStr: String,

                @SerializedName("name")
                @Expose
                val name: String,

                @SerializedName("screen_name")
                @Expose
                val screenName: String,

                @SerializedName("profile_image_url")
                @Expose
                val profileImageUrl: String,

                @SerializedName("profile_image_url_https")
                @Expose
                val profileImageUrlHttps: String)