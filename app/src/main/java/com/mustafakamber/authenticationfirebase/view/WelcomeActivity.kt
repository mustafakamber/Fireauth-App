package com.mustafakamber.authenticationfirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mustafakamber.authenticationfirebase.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var binding : ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.welcomeProgress.visibility = View.INVISIBLE

        auth = FirebaseAuth.getInstance()

        var currentUser = auth.currentUser

        //Giriş Yap Butonu islemleri
        binding.welcomeLoginButton.setOnClickListener {
            if(TextUtils.isEmpty(binding.welcomeEmailText.text.toString())){
                binding.welcomeEmailText.error = "E-mail adresinizi giriniz!"
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(binding.welcomePasswordText.text.toString())){
                binding.welcomePasswordText.error = "Şifrenizi giriniz!"
                return@setOnClickListener
            }
            else{
                //Bilgileri doğrulayıp giriş yapma işlemleri
                auth.signInWithEmailAndPassword(binding.welcomeEmailText.text.toString()
                    ,binding.welcomePasswordText.text.toString())
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            binding.welcomeProgress.visibility = View.VISIBLE
                            getProfileActivity()
                        }else{
                            Toast.makeText(this@WelcomeActivity
                                ,"Geçersiz e-mail veya şifre",Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }
        }
        //Kayıt Ol Butonu
        binding.welcomeSignupButton.setOnClickListener {
            getSignupActivity()
        }
        //Şifremi Unuttum Butonu
        binding.welcomeForgetPasswordText.setOnClickListener {
            getResetActivity()
        }

    }

    private fun getProfileActivity(){
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@WelcomeActivity, ProfileActivity::class.java))
            finish()
        },1500)
    }
    private fun getSignupActivity(){
        startActivity(Intent(this@WelcomeActivity, SignupActivity::class.java))
    }
    private fun getResetActivity(){
        startActivity(Intent(this@WelcomeActivity, ResetActivity::class.java))
    }



}