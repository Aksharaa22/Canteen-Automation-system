package com.example.canteenmad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Dashboard : AppCompatActivity() {

    var array = arrayOf("Breakfast","Lunch","Evening Tiffin","Snack","Beverages")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val m: ListView = findViewById(R.id.l1)
        val fb= FirebaseAuth.getInstance().getCurrentUser()?.getUid()


        val a = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, array)
        m.adapter = a
        m.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val i = m.getItemAtPosition(position) as String
            if(i=="Breakfast"){
                val i1 = Intent(this@Dashboard, DisplayMenuCart::class.java)
                i1.putExtra("type",i)
                i1.putExtra("id",fb)
                startActivity(i1)
            }
            else if(i=="Lunch"){
                val i1 = Intent(this@Dashboard, DisplayMenuCart::class.java)
                i1.putExtra("type",i)
                i1.putExtra("id",fb)
                startActivity(i1)
            }
            else if(i=="Evening Tiffin"){
                val i1 = Intent(this@Dashboard, DisplayMenuCart::class.java)
                i1.putExtra("type",i)
                i1.putExtra("id",fb)
                startActivity(i1)
            }
            else if(i=="Beverages"){
                val i1 = Intent(this@Dashboard, DisplayMenuCart::class.java)
                i1.putExtra("type",i)
                i1.putExtra("id",fb)
                startActivity(i1)
            }
            else{
                val i1 = Intent(this@Dashboard, DisplayMenuCart::class.java)
                i1.putExtra("type",i)
                i1.putExtra("id",fb)
                startActivity(i1)
            }

        }

    }
}