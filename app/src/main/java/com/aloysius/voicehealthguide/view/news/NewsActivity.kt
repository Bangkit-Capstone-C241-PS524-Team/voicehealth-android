package com.aloysius.voicehealthguide.view.news

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aloysius.voicehealthguide.R
import com.aloysius.voicehealthguide.databinding.ActivityNewsBinding
import com.aloysius.voicehealthguide.view.ViewModelFactory
import com.aloysius.voicehealthguide.data.remote.response.ArticlesItem
import com.aloysius.voicehealthguide.databinding.ActivityMainBinding
import com.aloysius.voicehealthguide.view.main.MainActivity
import com.aloysius.voicehealthguide.view.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.carousel.CarouselLayoutManager

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val newsViewModel by viewModels<NewsViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = CarouselLayoutManager()
        binding.recycleView.layoutManager = layoutManager
        bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.selectedItemId = R.id.item_1
        newsViewModel.getListNews()
        newsViewModel.listNews.observe(this) { news ->
            displayNews(news)
        }
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_2 -> {
                    val intent = Intent(this, MainActivity::class.java)
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
        setupView()

    }

    private fun displayNews(news: List<ArticlesItem>) {
        val adapter = NewsAdapter()
        binding.recycleView.adapter = adapter
        adapter.submitList(news)
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
