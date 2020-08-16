package com.gne.twitter.ui.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gne.twitter.KEY_LOGIN_STATUS
import com.gne.twitter.R
import com.gne.twitter.convertDpToPixel
import com.gne.twitter.custom.OnPageScrollListener
import com.gne.twitter.custom.Status
import com.gne.twitter.databinding.ActivityMainBinding
import com.gne.twitter.retrofit.response.tweets.TweetsResponse
import com.gne.twitter.saveSharedPref
import com.gne.twitter.ui.adapters.AdapterTweets
import com.gne.twitter.vm.TweetsViewModel
import com.google.android.material.snackbar.Snackbar
import com.twitter.sdk.android.core.TwitterCore


class ActivityMain : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var isLoading = false
    private var hasReachedEnd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val session = TwitterCore.getInstance().sessionManager.activeSession
        val userName = session.userName
        supportActionBar!!.setTitle("Welcome, $userName")

        monitorNetwork()

        val tweetsViewModel = ViewModelProvider(this).get(TweetsViewModel::class.java)

        val adapterTweets = AdapterTweets(tweetsViewModel.tweetList)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerTweets.layoutManager = linearLayoutManager
        binding.recyclerTweets.adapter = adapterTweets

        tweetsViewModel.listLiveData.observe(this, Observer { tweetsResponse: TweetsResponse ->
            binding.groupLoading.visibility= View.GONE
            binding.recyclerTweets.visibility=View.VISIBLE
            binding.progressbarList.visibility=View.GONE
            if(tweetsResponse.status==Status.SUCCESS) {
                isLoading = false
                if (tweetsResponse.tweetsList!!.isNotEmpty()) {
                    tweetsViewModel.addTweets(tweetsResponse.tweetsList)
                    adapterTweets.notifyItemRangeInserted(adapterTweets.itemCount, tweetsResponse.tweetsList.size)
                } else {
                    hasReachedEnd = true
                    setRecyclerPadding()
                    showSnackBar("No More Tweets!")
                }
            }
            else if(tweetsResponse.status==Status.ERROR){
                setRecyclerPadding()
                showSnackBar(tweetsResponse.errorMessage)
            }
            else{
                setRecyclerPadding()
                showSnackBar("Something Went Wrong.")
            }
        })

        binding.recyclerTweets.addOnScrollListener(object : OnPageScrollListener(linearLayoutManager) {
            override fun loadNewData() {
                if (!isLoading && !hasReachedEnd) {
                    isLoading = true
                    setRecyclerPaddingBottom()
                    binding.progressbarList.visibility=View.VISIBLE
                    tweetsViewModel.getListLiveDataSince()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                TwitterCore.getInstance().sessionManager.clearActiveSession()
                saveSharedPref(this, KEY_LOGIN_STATUS, false)
                val intent = Intent(this, ActivityLogin::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }

    fun monitorNetwork(){
        val networkCallback: NetworkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                hasReachedEnd = false
            }

            override fun onLost(network: Network) {
//                hasReachedEnd = true
            }
        }

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
    }

    fun showSnackBar(message: String){
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("Ok") { snackbar.dismiss() }
        snackbar.show()
    }

    fun setRecyclerPadding(){
        val dp=convertDpToPixel(4,this)
        binding.recyclerTweets.setPadding(dp,dp,dp,dp)
    }

    fun setRecyclerPaddingBottom(){
        val dp=convertDpToPixel(4,this)
        val bottomDp=convertDpToPixel(60,this)
        binding.recyclerTweets.setPadding(dp,dp,dp,bottomDp)
    }
}