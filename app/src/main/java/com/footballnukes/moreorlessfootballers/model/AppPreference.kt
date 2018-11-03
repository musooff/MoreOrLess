package com.footballnukes.moreorlessfootballers.model

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo

class AppPreference(private var context: Context) {

    companion object {
        const val LAST_GAME = "last"
    }

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("Download Data", 0)
    private var editor: SharedPreferences.Editor

    init {
        editor = sharedPreferences.edit().apply {  }
    }

    fun getHighScores(category: String): Int{
        return sharedPreferences.getInt(category, 0)
    }

    fun setHighScores(category: String, score: Int){
        editor.putInt(category, score).apply()
    }

    fun setLastGame(name: String) {
        editor.putString(LAST_GAME, name).apply()
    }

    fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
}