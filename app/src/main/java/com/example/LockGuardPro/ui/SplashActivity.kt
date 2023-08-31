package com.example.LockGuardPro.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.LockGuardPro.local.Preferences
import com.example.LockGuardPro.ui.applock.PassActivtiy.Companion.PASS_LOGIN
import com.example.LockGuardPro.ui.applock.PassTypeActivity
import com.thn.LockGuardPro.R

class SplashActivity : AppCompatActivity() {
    private lateinit var preferences: Preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        preferences = Preferences.getInstance(this)
        Handler(Looper.getMainLooper()).postDelayed({
            if (preferences.getString(PASS_LOGIN).isNullOrEmpty()) {
                startActivity(Intent(this@SplashActivity, PassTypeActivity::class.java))

            } else
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 3000)
    }
}