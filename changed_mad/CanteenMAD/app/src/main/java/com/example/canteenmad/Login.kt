package com.example.canteenmad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
class Login : AppCompatActivity() {
    var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var loginbtn = findViewById<Button>(R.id.button)
        var email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        var password = findViewById<EditText>(R.id.editTextTextPassword)
var fog=findViewById<TextView>(R.id.textView9)
        fog.setOnClickListener{
            //Toast.makeText(this@Login,"pop",Toast.LENGTH_SHORT).show()
            if(email.text.trim().toString().isNotEmpty())
            {
                auth.sendPasswordResetEmail(email.text.trim().toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@Login, "Mail sent", Toast.LENGTH_SHORT).show()
                            // Log.d(TAG, "Email sent.");
                        }
                    }

            }
            else
            {
                Toast.makeText(this@Login, "Email Field is Empty!!", Toast.LENGTH_SHORT).show()
            }
        }
        loginbtn.setOnClickListener{
            if(email.text.trim().toString().isNotEmpty() || password.text.trim().toString().isNotEmpty()){
                login(email.text.trim().toString(), password.text.trim().toString())
            }
            else{
                Toast.makeText(this, "Input required!", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun login(email:String, password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    if(email=="deepikrish24@gmail.com" && password=="deepika") {
                        var intent = Intent(this, BillDisplay::class.java);
                        //var intent = Intent(this, MenuAdd::class.java);
                        startActivity(intent)
                    }
                    else{
                        var intent = Intent(this, Dashingboard::class.java);
                        startActivity(intent)
                    }
                }
                else{
                    Toast.makeText(this, "Password does not match to the Email", Toast.LENGTH_LONG).show()
                }
            }
    }

}