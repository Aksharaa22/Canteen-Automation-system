package com.example.canteenmad

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class EmenuAdd : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var type=""
 lateinit var imageuri: Uri
 lateinit var file_uri:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emenu_add)
        var imag=findViewById<TextView>(R.id.textView2)
        var additem = findViewById<Button>(R.id.Add)
        var name = findViewById<EditText>(R.id.Itemname)
        val price = findViewById<EditText>(R.id.Price)
        val quan = findViewById<EditText>(R.id.Quantity)
        var spinner: Spinner = findViewById(R.id.spinner)
imag.setOnClickListener{
    selectImage()
}
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.types,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        additem.setOnClickListener{
            savef(name.text.toString(),type.toString(),price.text.toString().toInt(),quan.text.toString().toInt())
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Please select..."
            ),
            100
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100 && resultCode== RESULT_OK){
            imageuri = data?.data!!
var im =findViewById<ImageView>(R.id.back)
            im.setImageURI(imageuri)
        }
    }



    private fun savef(name: String, desc: String, price: Int, quan: Int) {
        val formatter=SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now=Date()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val add = HashMap<String,Any>()
        add["ItemName"] = name
        add["Description"] = desc
        add["Price"]=price
        add["Quantity"]=quan
        val fileName=formatter.format(now)
        val storageReference= FirebaseStorage.getInstance().reference.child(fileName)
       /* storageReference.child(fileName).putFile(imageuri).addOnSuccessListener {
        val result = it.metadata!!.reference!!.downloadUrl;
            add["Image"]=result
            Toast.makeText(this," result", Toast.LENGTH_LONG).show()
        }*/
        storageReference.putFile(imageuri!!)
            .addOnSuccessListener {
                val result = it.metadata!!.reference!!.downloadUrl;
                result.addOnSuccessListener {

                    val imageLink = it.toString()


                }
            }
        //StorageReference ref = FirebaseStorage().Ref().Child(fileName);
        //val url = (ref.getDownloadURL()).toString();
//val st=FirebaseStorage.getInstance().reference.child(fileName).downloadUrl
        //var uploadTask = storageReference.putFile(imageuri)
         //var downloadUri:String=storageReference.getDownloadURL()


        //add["Image"]=st
        /*val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {

                    throw it
                }
            }
            storageReference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //downloadUri = task.result.toString()
                //StorageReference ref = FirebaseStorage ('gs://your-id.appspot.com/images/cross.png').ref().child ();
                //url = (await ref.getDownloadURL()).toString();

            }
        }*/



//Toast.makeText(this,name, Toast.LENGTH_LONG).show()
        db.collection(desc)
            .add(add)
            .addOnSuccessListener {
                Toast.makeText(this,"Item added ", Toast.LENGTH_LONG).show()
                val intent = Intent(this@EmenuAdd, EmenuDisplay::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this," Item not added ", Toast.LENGTH_LONG).show()
            }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()
        type=text;
    }
}