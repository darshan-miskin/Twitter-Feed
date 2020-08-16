package com.gne.twitter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gne.twitter.R
import com.gne.twitter.databinding.LayoutTweetsRecyclerBinding
import com.gne.twitter.retrofit.response.tweets.Tweet
import com.gne.twitter.ui.adapters.AdapterTweets.TweetsHolder
import java.util.ArrayList

class AdapterTweets(private val tweetList: MutableList<Tweet>) : RecyclerView.Adapter<TweetsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: LayoutTweetsRecyclerBinding = DataBindingUtil.inflate(inflater, R.layout.layout_tweets_recycler, parent, false)
        return TweetsHolder(binding)
    }

    override fun onBindViewHolder(holder: TweetsHolder, position: Int) {
        holder.binding.tweet = tweetList.get(position)
    }

    override fun getItemCount(): Int {
        return tweetList.size
    }

    inner class TweetsHolder(val binding: LayoutTweetsRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

}