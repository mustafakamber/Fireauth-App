package com.mustafakamber.authenticationfirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mustafakamber.authenticationfirebase.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference?=null
    var database:FirebaseDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupProgress.visibility = View.INVISIBLE

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        //Kayıt Ol ve Giriş Yapma İşlemleri
        binding.signupAndLoginButton.setOnClickListener {

            //Boş text kontrolü
            if(TextUtils.isEmpty(binding.signupNameText.text.toString())){
                binding.signupNameText.error = "Adınızı girmeyi unutmayın!"
                return@setOnClickListener
            }else if (TextUtils.isEmpty(binding.signupEmailText.text.toString())){
                binding.signupEmailText.error = "E-mail adresinizi giriniz!"
                return@setOnClickListener
            }else if (TextUtils.isEmpty(binding.signupPasswordText.text.toString())){
                binding.signupPasswordText.error = "Şifrenizi giriniz!"
                return@setOnClickListener
            }

            //Email,şifre ve kullanıcı bilgilerini veritabanına ekleme
            auth.createUserWithEmailAndPassword(binding.signupEmailText.text.toString()
                ,binding.signupPasswordText.text.toString())
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        //Kullanıcı bilgilerini alma
                        var currentUser = auth.currentUser
                        //Şuanki kullanıcının id'sine erişip veri kaydetme
                        var currentUserDb = currentUser?.let { it1 ->
                            databaseReference?.child(it1.uid) }
                        currentUserDb?.child("name")
                        ?.setValue(binding.signupNameText.text.toString())

                        object : CountDownTimer(1750,1000){
                            override fun onTick(millisUntilFinished: Long){
                                binding.signupProgress.visibility = View.VISIBLE
                            }
                            override fun onFinish() {
                                startActivity(Intent(this@SignupActivity, ProfileActivity::class.java))
                                finish()
                            }

                        }.start()


                    }else{
                       Toast.makeText(this@SignupActivity
                           ,"Kayıt işlemi başarısız",Toast.LENGTH_LONG)
                           .show()
                    }

                }


        }

    }


}









