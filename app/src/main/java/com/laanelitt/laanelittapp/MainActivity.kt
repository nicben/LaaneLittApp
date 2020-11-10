package com.laanelitt.laanelittapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.os.postDelayed
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "LåneLitt"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        setupViews()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){


            R.id.action_settings -> Toast.makeText(this,"settings", Toast.LENGTH_SHORT).show()

        }

        return super.onOptionsItemSelected(item)
    }

    fun setupViews()
    {
        var navHostFragment = supportFragmentManager.findFragmentById(
            R.id.myNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)

        //var appBarConfiguration = AppBarConfiguration(navHostFragment.navController.graph)
        var appBarConfiguration = AppBarConfiguration(setOf(
            R.id.searchPageFragment, R.id.addAssetFragment, R.id.myAssetsFragment))
        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
    }

    companion object {
        @JvmStatic
        fun visSnackbar(view: View?, melding: String?) {
            Snackbar.make(view!!, melding!!, Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }
    }
}