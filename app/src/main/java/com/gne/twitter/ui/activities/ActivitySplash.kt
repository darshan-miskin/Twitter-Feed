package com.gne.twitter.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gne.twitter.*
import com.gne.twitter.R
import com.gne.twitter.databinding.ActivitySplashBinding

class ActivitySplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        Handler().postDelayed({
            val intent: Intent
            intent = if (getSharedPrefBool(this, KEY_LOGIN_STATUS)) {
                Intent(this, ActivityMain::class.java)
            } else {
                Intent(this, ActivityLogin::class.java)
            }
            startActivity(intent)
            finish()
        }, 3 * 1000.toLong())
    }
}