package com.app.salimos

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private lateinit var back:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        txtName = findViewById(R.id.txtName)
        txtLastName = findViewById(R.id.txtLastName)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        back = findViewById(R.id.backToMain)
        progressBar = findViewById(R.id.progressBar)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        dbReference = database.reference.child("User")

        back.setOnClickListener(){
            onBackPressed()
        }

    }

    fun register(view: View) {
        createNewAccount()
    }

    private fun createNewAccount() {
        val name: String = txtName.text.toString()
        val lastName: String = txtLastName.text.toString()
        val email: String = txtEmail.text.toString()
        val password: String = txtPassword.text.toString()


        if (Validator.isValidName(findViewById(R.id.txtName), true) && Validator.isValidName(
                findViewById(R.id.txtLastName),
                true
            ) && Validator.isValidEmail(
                findViewById(R.id.txtEmail),
                true
            ) && Validator.isValidPassword(findViewById(R.id.txtPassword), true)
        ) {
            progressBar.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isComplete) {
                        val user: FirebaseUser? = auth.currentUser
                        verifyEmail(user)

                        val userBD = dbReference.child(user?.uid.toString())
                        userBD.child("Name").setValue(name)
                        userBD.child("LastName").setValue(lastName)
                        userBD.child("admin").setValue(0)
                        action()
                    }
                }
        }
    }

    private fun action() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun verifyEmail(user: FirebaseUser?) {
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->

                if (task.isComplete) {
                    Toast.makeText(this, "Email enviado", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error al enviar el email", Toast.LENGTH_LONG).show()
                }
            }
    }
}


