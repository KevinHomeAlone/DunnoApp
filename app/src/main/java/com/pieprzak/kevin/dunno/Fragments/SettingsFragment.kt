package com.pieprzak.kevin.dunno.Fragments


import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pieprzak.kevin.dunno.Login.LoginActivity

import com.pieprzak.kevin.dunno.R
import com.pieprzak.kevin.dunno.Utilities.SharedPreferencesConnection
import kotlinx.android.synthetic.main.fragment_settings.view.*

private const val ARG_USER_ID = "param_user_id"


class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        view.button_logout.setOnClickListener {
            logoutUser()
        }
        // Inflate the layout for this fragment
        return view
    }

    fun logoutUser(){
        SharedPreferencesConnection.clearSharedPreferences(activity)
        runLoginActivity()
    }

    private fun runLoginActivity() {

        Log.d("Logout", "Running LoginActivity")

        val intent = Intent(activity, LoginActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity.finish()
    }


}
