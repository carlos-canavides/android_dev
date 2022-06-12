package com.example.androidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var topAppBar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        topAppBar = findViewById(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        val drawer_layout: DrawerLayout = findViewById(R.id.drawer_layout)
        val nav_drawer: NavigationView = findViewById(R.id.nav_drawer)
        toggle = ActionBarDrawerToggle(this, drawer_layout, topAppBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        initNavComponent(nav_drawer)

    }

    private fun initNavComponent(nav: NavigationView) {
        nav.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            var selectedFragment: Fragment? = null
            var titulo: String = "Android"
            when (menuItem.itemId) {
            }
            setTitle(titulo)
            replaceFragment(selectedFragment)
            true
        })
    }

    private fun replaceFragment(selectedFragment: Fragment?) {
        if (selectedFragment != null) {
            val manager: FragmentManager = supportFragmentManager
            manager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
        }
    }
}