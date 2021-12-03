package com.example.canteenmad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EmenuDisplay : AppCompatActivity() {
    var type=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var add = findViewById<Button>(R.id.Add)
     /*   var logout1=findViewById<Button>(R.id.Logout)
        val b:Bundle ?= intent.extras
         //var a1:String = b?.getString(1.toString()).toString()
        //val a2:String = b?.getString(2.toString()).toString()
        val v1 : String = intent.getStringExtra("type").toString()
        type=v1
        Toast.makeText(this@EmenuDisplay,type,Toast.LENGTH_LONG).show()
        setContentView(R.layout.activity_emenu_display)
        readFireStoreData()
        logout1.setOnClickListener{
            var intent = Intent(this, Login::class.java);
            startActivity(intent)*/
        }
    }
    private fun readFireStoreData() {
      /*  val db = FirebaseFirestore.getInstance()
        val d = findViewById<TextView>(R.id.textView3)

        db.collection(type)
            .get()
            .addOnCompleteListener {
                val result = StringBuffer()
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        //result.append(document.data.getValue("ItemName")).append(" ")
                            //.append(document.data.getValue("Price")).append("\n\n")


                    }
                    d.setText(result).toString()
                }
            }
    }*/
}
    /*fun readFireStoreData() {
        var d=findViewById<ListView>(R.id.l1)
        val m: ListView = findViewById(R.id.l1)
        val db = FirebaseFirestore.getInstance()
        val b= arrayOf("")
        db.collection("Menu")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    for(document in it.result!!) {
                        if(document.data.getValue("Description")=="Breakfast") {
                            val result: StringBuffer = StringBuffer()
                            result.append(document.data.getValue("ItemName")).append(" ")
                                .append(document.data.getValue("Price")).append("\n\n")
                            b[b.size]= result.toString()
                        }
                    }
                    val a = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, b)
                    m.adapter = a
                }
            }*/

