package com.fatherofapps.androidbase.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.activities.BaseActivity
import com.fatherofapps.androidbase.common.AppSharePreference
import com.fatherofapps.androidbase.ui.home.HomeFragment
import com.fatherofapps.androidbase.ui.home.TextFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(){

    @Inject
    lateinit var preferences: SharedPreferences
    private var loadingLayout: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("Frank","MainActivity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadingLayout = findViewById(R.id.loadingLayout)
        var bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        if (preferences.getBoolean(AppSharePreference.FIRST_LAUNCH, true)) {
            Log.e("AAA", preferences.getBoolean(AppSharePreference.FIRST_LAUNCH, true).toString())
            nav().popBackStack(R.id.searchFragment, true)
            nav().navigate(R.id.introFragment)
        } else {
            nav().popBackStack(R.id.searchFragment, true)
            nav().navigate(R.id.homeFragment)
        }

        bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.voice -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.text -> {
                    replaceFragment(TextFragment())
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    fun nav(): NavController {
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        return navHostFragment.navController
    }

    override fun showLoading(isShow: Boolean) {
        loadingLayout?.visibility = if(isShow) View.VISIBLE else View.GONE
    }


}