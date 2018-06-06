package com.pieprzak.kevin.dunno

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.pieprzak.kevin.dunno.Fragments.QuestionsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mapOfFragments = mutableMapOf<String, Fragment>()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if(!item.isChecked){
            when (item.itemId) {
                R.id.navigation_questions -> {
                    Log.d("Navigation", "Swap fragment to QuestionsFragment")
                    fragmentManager.popBackStack()
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_fragment_placeholder, mapOfFragments[getString(R.string.dunnos)] as QuestionsFragment)
                            .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
        }
        false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createFragments()

        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_fragment_placeholder, mapOfFragments[getString(R.string.dunnos)])
                    .commit()
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun createFragments() {
        mapOfFragments[getString(R.string.dunnos)] = QuestionsFragment()
    }
}
