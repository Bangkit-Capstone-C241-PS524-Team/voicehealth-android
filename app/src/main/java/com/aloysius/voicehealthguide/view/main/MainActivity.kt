package com.aloysius.voicehealthguide.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.databinding.ActivityMainBinding
import com.aloysius.voicehealthguide.view.ViewModelFactory
import com.aloysius.voicehealthguide.view.news.NewsActivity
import com.aloysius.voicehealthguide.view.profile.ProfileActivity
import com.aloysius.voicehealthguide.view.register.RegisterActivity
import com.aloysius.voicehealthguide.view.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.selectedItemId = R.id.item_2
        lifecycleScope.launch {
            viewModel.getSession().observe(this@MainActivity) { user ->
                if (!user.isLogin) {
                    navigateToWelcome()
                } else {
                    // Perform any other actions if needed
                }
            }
        }

        setupView()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_1 -> {
                    val intent = Intent(this, NewsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_3 -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun navigateToWelcome() {
        val intent = Intent(this, WelcomeActivity::class.java)
        Log.d("MainActivity", "Navigating to WelcomeActivity")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}
