package com.mustafakamber.authenticationfirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mustafakamber.authenticationfirebase.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateBinding
    private lateinit var auth : FirebaseAuth
    var databaseReference : DatabaseReference?=null
    var database : FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.updateProgress.visibility = View.INVISIBLE

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        //Aktif Kullanıcıya erişme
        var currentUser = auth.currentUser
        binding.updateEmailText.setText(currentUser?.email)

        //Realtime db'deki aktif kullanıcı verilerini çekme
        var currentUserReference = databaseReference?.child(currentUser?.uid!!)
        currentUserReference?.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.updateNameText.setText(snapshot.child("name").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        //Güncelle Butonu
        binding.updateButton.setOnClickListener {
            //Boş Text Kontrolü
            if(TextUtils.isEmpty(binding.updateNameText.text.toString())){
                binding.updateNameText.error = "Adınızı girmeyi unutmayın!"
                return@setOnClickListener
            }else if (TextUtils.isEmpty(binding.updateEmailText.text.toString())){
                binding.updateEmailText.error = "E-mail adresinizi giriniz!"
                return@setOnClickListener
            }else if (TextUtils.isEmpty(binding.updatePasswordText.text.toString())){
                binding.updatePasswordText.error = "Şifrenizi giriniz!"
                return@setOnClickListener
            }

            //Ad-Soyad Güncelleme
            var currentUserDb = currentUser?.let { it1 ->
                databaseReference?.child(it1.uid) }
            currentUserDb?.removeValue()
            currentUserDb?.child("name")?.setValue(binding.updateNameText.text.toString())
            binding.updateProgress.visibility = View.VISIBLE


            //Şifre Güncelleme
            currentUser!!.updatePassword(binding.updatePasswordText.text.toString().trim())
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        //Mail Güncelleme
                        currentUser!!.updateEmail(binding.updateEmailText.text.toString().trim())
                            .addOnCompleteListener{
                                if(it.isSuccessful){

                                }else{
                                    Log.e("error: ",it.exception.toString())
                                    Toast.makeText(this@UpdateActivity,"E-mail Adresiniz Güncellenemedi",
                                        Toast.LENGTH_LONG).show()
                                }
                            }
                    }else{
                        Log.e("error: ",it.exception.toString())
                        Toast.makeText(this@UpdateActivity,"Şifreniz Güncellenemedi",
                            Toast.LENGTH_LONG).show()
                    }
                }

            getWelcomeActivity()
        }
    }

    private fun getWelcomeActivity(){
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@UpdateActivity, WelcomeActivity::class.java))
            finish()
        },1500)
    }
}