package com.pieprzak.kevin.dunno.Utilities

import android.content.Context

object SharedPreferencesConnection{

    private const val PREFS_FILENAME = "com.pieprzak.kevin.dunno"
    private const val PREFS_TAG_ACCESS_TOKEN = "access_token"
    private const val PREFS_TAG_LOGIN = "login"

    fun clearSharedPreferences(context: Context) {
        removeAccessToken(context)
        removeLogin(context)
    }

    fun setAccessToken(context: Context, accessToken: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        sharedPreferences.edit()
                .putString(PREFS_TAG_ACCESS_TOKEN, accessToken)
                .apply()
    }

    fun getAccessToken(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PREFS_TAG_ACCESS_TOKEN, "")
    }

    fun removeAccessToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        sharedPreferences.edit()
                .remove(PREFS_TAG_ACCESS_TOKEN)
                .apply()

    }

    fun setLogin(context: Context, login: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        sharedPreferences.edit()
                .putString(PREFS_TAG_LOGIN, login)
                .apply()
    }

    fun getLogin(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PREFS_TAG_LOGIN, "")
    }

    fun removeLogin(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        sharedPreferences.edit()
                .remove(PREFS_TAG_LOGIN)
                .apply()

    }
}