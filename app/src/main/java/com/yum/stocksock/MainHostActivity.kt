package com.yum.stocksock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yum.stocksock.databinding.ActivityMainBinding

class MainHostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}