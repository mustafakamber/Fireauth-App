package com.mustafakamber.authenticationfirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.mustafakamber.authenticationfirebase.databinding.ActivityResetBinding

class ResetActivity : AppCompatActivity() {
    lateinit var binding: ActivityResetBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        //Gönder Butonu
        binding.resetSendButton.setOnClickListener {
            if(TextUtils.isEmpty(binding.resetEmailText.text.toString().trim())){
                binding.resetEmailText.error = "E-mail adresinizi giriniz!"
            }else{
                //Sıfırlama Bağlantısı Gönderme ve Şifre Sıfırlama
                auth.sendPasswordResetEmail(binding.resetEmailText.text.toString().trim())
                    .addOnCompleteListener(this){ reset ->
                        if(reset.isSuccessful){
                            binding.resetInfoText.text = "E-mail adresinize sıfırlama bağlantısı gönderildi.Lütfen kontrol ediniz."
                        }
                        else{
                            binding.resetInfoText.text = "Sıfırlama bağlantısı gönderilemedi.Tekrar deneyiniz."
                        }

                    }
            }

        }

        //Giriş Ekranına Dön Butonu
        binding.resetLoginButton.setOnClickListener {
            getWelcomeActivity()
        }

    }
    private fun getWelcomeActivity(){
        startActivity(Intent( this@ResetActivity, WelcomeActivity::class.java))
        finish()
    }
}