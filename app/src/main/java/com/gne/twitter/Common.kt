package com.gne.twitter

import android.content.Context
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import androidx.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView

private const val SHARED_PREF_NAME = "my_shared_pref"
const val KEY_ID = "my_key"
const val KEY_NAME = "my_name"
const val KEY_TOKEN = "my_token"
const val KEY_LOGIN_STATUS = "my_login_status"

fun saveSharedPref(context: Context, key: String?, value: String?) {
    val sharedPreferences = context.applicationContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(key, value)
    editor.apply()
}

fun saveSharedPref(context: Context, key: String?, value: Long) {
    val sharedPreferences = context.applicationContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putLong(key, value)
    editor.apply()
}

fun saveSharedPref(context: Context, key: String?, value: Boolean) {
    val sharedPreferences = context.applicationContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean(key, value)
    editor.apply()
}

fun getSharedPref(context: Context, key: String?): String? {
    val sharedPreferences = context.applicationContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, "-1")
}

fun getSharedPrefLong(context: Context, key: String?): Long {
    val sharedPreferences = context.applicationContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getLong(key, -1)
}

fun getSharedPrefBool(context: Context, key: String?): Boolean {
    val sharedPreferences = context.applicationContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(key, false)
}

fun convertDpToPixel(dp: Int, context: Context): Int {
    return dp * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

fun convertPixelsToDp(px: Float, context: Context): Int {
    return px.toInt() / (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

fun isNetworkAvailable(context: Context): Boolean {
    var haveConnectedWifi = false
    var haveConnectedMobile = false
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            ?: return false
    val netInfo = connectivityManager.allNetworkInfo
    for (ni in netInfo) {
        if (ni.typeName.equals("WIFI", ignoreCase = true)) if (ni.isConnected) haveConnectedWifi = true
        if (ni.typeName.equals("MOBILE", ignoreCase = true)) if (ni.isConnected) haveConnectedMobile = true
    }
    return haveConnectedWifi || haveConnectedMobile
}

@BindingAdapter("app:imageUrl")
fun loadImage(view: SimpleDraweeView, imageUrl: String) {
    view.setImageURI(imageUrl)
}
