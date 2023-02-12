package com.mustafakamber.authenticationfirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import com.google.firebase.auth.FirebaseAuth
import com.mustafakamber.authenticationfirebase.R
import com.mustafakamber.authenticationfirebase.databinding.ActivityResetBinding
import com.mustafakamber.authenticationfirebase.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        //Yanıp sönen firebase logosu
        val blinkAnimation = AnimationUtils.loadAnimation(this@SplashActivity,R.anim.animation)
        binding.splashImage.startAnimation(blinkAnimation)

        //Aktif kullanıcı kontrolu
        var currentUser = auth.currentUser
        if(currentUser != null){
            getProfileActivity()
        }else{
            getWelcomeActivity()
        }

    }

    private fun getProfileActivity(){
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, ProfileActivity::class.java))
            finish()
        },2750)
    }

    private fun getWelcomeActivity(){
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
            finish()
        },2750)
    }
}