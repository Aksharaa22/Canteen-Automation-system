package com.example.canteenmad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class ModifyingFormDisplay extends AppCompatActivity {
String name,type;
Button bt;
EditText quan,price;
TextView itm ;
FirebaseFirestore fb1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifying_form_display);
        Bundle b= getIntent().getExtras();
        name = b.getString("item");
        type=b.getString("type");
        quan=(EditText) findViewById(R.id.editTextTextPersonName);
        price=(EditText) findViewById(R.id.editTextTextPersonName2);
        itm= (TextView) findViewById(R.id.textView5);
        bt=(Button)findViewById(R.id.button4);
        fb1= FirebaseFirestore.getInstance();
        //DocumentReference doc=fb1.collection(type).document(itemname);
        fb1.collection(type)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(DocumentSnapshot snapshot: task.getResult()){
                            //list.add(snapshot.getId() );
                            String t=snapshot.getId();
                            if(t.equals(name)) {
                                double rr= snapshot.getDouble("Quantity");
                                String r=String.valueOf(rr);
                                quan.setText(r);
                                itm.setText(name);
                                double i=snapshot.getDouble("Price");
                                String s=String.valueOf(i);

                               price.setText(s);
                            }


                        }



                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ModifyingFormDisplay.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    bt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Editable q;
            Editable p;
            q=quan.getText();
            p=price.getText();

            HashMap<String , Object> map=new HashMap<>();
            //map.put("ItemName","name");
int iq=Integer.parseInt(String.valueOf(q));
            int ip=Integer.parseInt(String.valueOf(p));
            map.put("Price",ip);
             map.put("Quantity",iq);
            fb1.collection(type).document(name).update(map);
        }
    });
    }
}