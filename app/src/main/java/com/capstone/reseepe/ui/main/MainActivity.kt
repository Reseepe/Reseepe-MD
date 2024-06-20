package com.capstone.reseepe.ui.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.capstone.reseepe.R
import com.capstone.reseepe.databinding.ActivityMainBinding
import com.capstone.reseepe.ui.welcome.WelcomeActivity
import com.capstone.reseepe.util.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show splash screen
        showSplashScreen()
    }

    private fun showSplashScreen() {
        // Set content view to splash screen layout
        setContentView(R.layout.splash_screen)

        // Optional: Customize splash screen appearance or animation
        val imageSplash = findViewById<ImageView>(R.id.imageSplash)
        // Example animation (fade in)
        ObjectAnimator.ofFloat(imageSplash, "alpha", 0f, 1f).apply {
            duration = 1000 // 1 second
            start()
        }

        // Simulate delay for splash screen (2 seconds)
        Handler().postDelayed({
            // Start MainActivity after splash screen
            initializeMainActivity() // Method to initialize MainActivity components
        }, 2500) // 2.5 seconds delay
    }

    private fun initializeMainActivity() {
        // Inflate the main layout and set it as content view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ViewModel
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        // Setup bottom navigation
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
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
}
