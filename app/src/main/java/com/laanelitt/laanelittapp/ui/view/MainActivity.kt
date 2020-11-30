package com.laanelitt.laanelittapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.laanelitt.laanelittapp.R
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //actionbar
        val actionbar = supportActionBar
        //setter actionbar tittel
        actionbar!!.title = "LåneLitt"
        //setter back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        setupViews()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupViews()
    {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.myNavHostFragment
        ) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)


        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homePageFragment,
            R.id.notificationsFragment,
            R.id.addAssetFragment,
            R.id.myAssetsListFragment
        ))
        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
    }
}




