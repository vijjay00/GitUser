package com.initial.gituser.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.initial.gituser.R

class SplashScreenActivity : AppCompatActivity() {

    private val mWaitHandler = Handler()
    private val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        mWaitHandler.postDelayed({
            try {
                startActivity(Intent(context, HomeActivity::class.java))
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 2300)
    }
}
