package com.example.labor_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.labor_1.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private val TAG = "SplashActivity"
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: SplashActivity created.")

        binding.startButton.setOnClickListener {
            val message = binding.messageEditText.text.toString()

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("user_message", message)
            }
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: SplashActivity started.")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: SplashActivity resumed.")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: SplashActivity paused.")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: SplashActivity stopped.")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: SplashActivity restarted.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: SplashActivity destroyed.")
    }
}
