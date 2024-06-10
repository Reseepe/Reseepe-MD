package com.capstone.reseepe.ui.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.reseepe.R
import com.capstone.reseepe.databinding.ActivityMainBinding
import com.capstone.reseepe.ui.profile.ProfileFragment
import com.capstone.reseepe.ui.welcome.WelcomeActivity
import com.capstone.reseepe.util.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), ProfileFragment.LogoutListener {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_scan,
                R.id.navigation_bookmarks,
                R.id.navigation_profile
            )
        )
        navView.setupWithNavController(navController)

    }

    override fun onLogout() {
        viewModel.logout()
    }

}