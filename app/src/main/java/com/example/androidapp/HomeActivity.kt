package com.example.androidapp

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.androidapp.fragments.HomeFragment
import com.example.androidapp.fragments.ListFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {
    BASIC
}

class HomeActivity : AppCompatActivity() {

    private lateinit var topAppBar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    private var activity: Activity = this
    private lateinit var text_username: TextView
    private lateinit var text_email: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        topAppBar = findViewById(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        val drawer_layout: DrawerLayout = findViewById(R.id.drawer_layout)
        val nav_drawer: NavigationView = findViewById(R.id.nav_drawer)
        toggle = ActionBarDrawerToggle(this, drawer_layout, topAppBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        val header: View = nav_drawer.getHeaderView(0)
        text_username = header.findViewById(R.id.user_name)
        text_email = header.findViewById(R.id.user_email)
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")
        if (email!=null && provider!=null) {
            setUp(email, provider)
        }
        initNavComponent(nav_drawer)
        replaceFragment(HomeFragment())
    }

    private fun initNavComponent(nav: NavigationView) {
        nav.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            var selectedFragment: Fragment? = null
            var titulo: String = "Android"
            when(menuItem.itemId){
                R.id.menu_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    activity.finish()
                };
                R.id.menu_home -> {
                    nav.setCheckedItem(R.id.menu_home)
                    selectedFragment = HomeFragment()
                    titulo = "Map"
                };
                R.id.menu_list -> {
                    nav.setCheckedItem(R.id.menu_list)
                    selectedFragment = ListFragment()
                    titulo = "List"
                };
            }
            activity.setTitle(titulo)
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

    private fun setUp(email: String, provider: String) {
        text_username.setText("Administrador")
        text_email.setText(email)
    }
}
