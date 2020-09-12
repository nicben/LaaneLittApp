package com.laanelitt.laanelittapp
/* MainActivity is an example of an Activity. An Activity is a core Android class that draws an
Android app user interface (UI) and receives input events. When your app launches, it launches the
activity specified in the AndroidManifest.xml file.

Many programming languages define a main method that starts the program. Android apps don't have a
main method. Instead, the AndroidManifest.xml file indicates that MainActivity should be launched
when the user taps the app's launcher icon. To launch an activity, the Android OS uses the
information in the manifest to set up the environment for the app and construct the MainActivity.
Then the MainActivity does some setup in turn.

Each activity has an associated layout file. The activity and the layout are connected by a process
known as layout inflation. When the activity starts, the views that are defined in the XML layout
files are turned into (or "inflated" into) Kotlin view objects in memory. Once this happens, the
activity can draw these objects to the screen and also dynamically modify them. */

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.laanelitt.laanelittapp.databinding.ActivityMainBinding

private lateinit var drawerLayout: DrawerLayout


class MainActivity : AppCompatActivity() {
    /*Activities do not use a constructor to initialize the object. Instead, a series of predefined
    methods (called "lifecycle methods") are called as part of the activity setup. One of those
    lifecycle methods is onCreate(), which you always override in your own app.
    In onCreate(), you specify which layout is associated with the activity, and you inflate the
    layout. The setContentView() method does both those things.*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}