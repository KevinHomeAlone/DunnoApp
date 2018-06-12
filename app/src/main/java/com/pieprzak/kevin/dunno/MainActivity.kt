package com.pieprzak.kevin.dunno

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.pieprzak.kevin.dunno.Fragments.QuestionsFragment
import com.pieprzak.kevin.dunno.Fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.bundleOf

private const val ARG_IS_USER_DUNNOS_FRAGMENT = "user_dunnos"

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
                R.id.navigation_settings -> {
                    Log.d("Navigation", "Swap fragment to SettingsFragment")
                    fragmentManager.popBackStack()
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_fragment_placeholder, mapOfFragments[getString(R.string.settings)] as SettingsFragment)
                            .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_my_dunnos -> {
                    Log.d("Navigation", "Swap fragment to QuestionsFragment - my dunnos")
                    fragmentManager.popBackStack()
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_fragment_placeholder, mapOfFragments[getString(R.string.my_dunnos)] as QuestionsFragment)
                            .commit()
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
        val bundleMyDunnos = bundleOf(Pair(ARG_IS_USER_DUNNOS_FRAGMENT, true))
        val bundleAllDunnos = bundleOf(Pair(ARG_IS_USER_DUNNOS_FRAGMENT, false))
        mapOfFragments[getString(R.string.dunnos)] = QuestionsFragment()
        mapOfFragments[getString(R.string.dunnos)]!!.arguments = bundleAllDunnos
        mapOfFragments[getString(R.string.settings)] = SettingsFragment()
        mapOfFragments[getString(R.string.my_dunnos)] = QuestionsFragment()
        mapOfFragments[getString(R.string.my_dunnos)]!!.arguments = bundleMyDunnos
    }
}
