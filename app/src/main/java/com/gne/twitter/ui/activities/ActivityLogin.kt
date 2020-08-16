package com.gne.twitter.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gne.twitter.*
import com.gne.twitter.R
import com.gne.twitter.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.loginButton.callback = object : Callback<TwitterSession?>() {
            override fun success(result: Result<TwitterSession?>) {
                saveSharedPref(this@ActivityLogin, KEY_LOGIN_STATUS, true)
                val intent = Intent(this@ActivityLogin, ActivityMain::class.java)
                startActivity(intent)
                finish()
            }

            override fun failure(exception: TwitterException) {
                val snackbar = Snackbar.make(findViewById(android.R.id.content), exception.message!!, BaseTransientBottomBar.LENGTH_INDEFINITE)
                snackbar.setAction("OK") { snackbar.dismiss() }
                snackbar.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.loginButton.onActivityResult(requestCode, resultCode, data)
    }
}