package com.mustafakamber.authenticationfirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mustafakamber.authenticationfirebase.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityProfileBinding
    private  lateinit var auth : FirebaseAuth
    var databaseReference : DatabaseReference?=null
    var database : FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        val alert = AlertDialog.Builder(this@ProfileActivity)

        //Aktif kullanıcının bilgilerini ilgili textlerde gösterme
        var currentUser = auth.currentUser
        binding.profileEmailText.text = "Email adresiniz: "+currentUser?.email

        //Realtime Db'deki childların içindeki veriyi çekme
        var currentUserDb = currentUser?.let { it1 ->
            databaseReference?.child(it1.uid) }
        currentUserDb?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //Database'deki verileri ilgili text'de gosterme
                binding.profileNameText.text = "Adınız: "+ snapshot.child("name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        //Çıkış Yap Butonu
        binding.profileLogoutButton.setOnClickListener {

            //Çıkış Yap AlertDialog

            alert.setTitle("Çıkış yapmak istediğinize emin misiniz?")
            alert.setNegativeButton("Hayır"){dialog,which ->

            }
            alert.setPositiveButton("Evet"){dialog,which ->
                auth.signOut()
                getWelcomeActivity()
            }
            alert.show()

        }

        //Hesabımı Sil Butonu
        binding.profileDeleteButton.setOnClickListener {

            //Hesap silme AlertDialog
            alert.setTitle("Hesabınızı silmek istediğinize emin misiniz?")
            alert.setNegativeButton("Hayır"){dialog,which ->

            }
            alert.setPositiveButton("Evet"){dialog,which ->
                currentUser?.delete()?.addOnCompleteListener {
                    if(it.isSuccessful){
                        //Aktif kullanıcı email-şifre bilgileri silme
                        auth.signOut()
                        //Aktif kullanıcının veritabanındaki bilgilerini silme
                        var currentUserDb = currentUser?.let { it ->
                            databaseReference?.child(it.uid) }
                        currentUserDb?.removeValue()

                        Toast.makeText(this@ProfileActivity,"Hesabınız Silindi",
                            Toast.LENGTH_LONG).show()
                        getWelcomeActivity()

                    }
                }
            }
            alert.show()


        }

        //Bilgilerimi Güncelle Butonu
        binding.profileUpdateButton.setOnClickListener {
            getUpdateActivity()
        }

        }



    private fun getWelcomeActivity(){
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@ProfileActivity, WelcomeActivity::class.java))
            finish()
        },2500)
        }
    private fun getUpdateActivity(){
        startActivity(Intent(this@ProfileActivity, UpdateActivity::class.java))
    }

}