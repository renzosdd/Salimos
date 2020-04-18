package com.app.salimos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var txtEmail: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpass)

        txtEmail=findViewById(R.id.txtEmail)
        auth=FirebaseAuth.getInstance()
        progressBar= findViewById(R.id.progressBar)
        back = findViewById(R.id.backToMain)

        back.setOnClickListener(){
            onBackPressed()
        }
    }
    fun send(view: View){
        val email=txtEmail.text.toString()

        if(!TextUtils.isEmpty(email)){
            progressBar.visibility=View.VISIBLE
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this){
                    task ->
                    if (task.isSuccessful){
                        startActivity(Intent(this,LoginActivity::class.java))
                    }else{
                        Toast.makeText(this,"Error al enviar el email", Toast.LENGTH_LONG).show()
                        progressBar.visibility=View.GONE
                    }
                }
        }
    }
}
