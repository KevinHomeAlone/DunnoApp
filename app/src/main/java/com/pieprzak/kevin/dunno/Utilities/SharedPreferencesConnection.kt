package com.pieprzak.kevin.dunno.Utilities

import android.content.Context

object SharedPreferencesConnection{

    private const val PREFS_FILENAME = "com.pieprzak.kevin.dunno"
    private const val PREFS_TAG_ACCESS_TOKEN = "access_token"
    private const val PREFS_TAG_LOGIN = "login"
    private const val PREFS_TAG_CLEAR_FLAG = "clear_flag"

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

    /*fun isCacheClearNeeded(context: Context): Boolean{
        val sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        return when(sharedPreferences.getString(PREFS_TAG_CLEAR_FLAG, "")){
            "true" -> true
            "false" -> false
            else -> true
        }
    }

    fun setCacheNeedClearFlag(context: Context, flag: Boolean){
        val sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val clearFlag = when(flag){
            true -> "true"
            else -> "false"
        }
        sharedPreferences.edit()
                .putString(PREFS_TAG_CLEAR_FLAG, clearFlag)
                .apply()
    }*/
}