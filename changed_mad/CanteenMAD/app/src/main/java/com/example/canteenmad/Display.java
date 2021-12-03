package com.example.canteenmad;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
public class Display extends AppCompatActivity {
    ListView coursesLV;
    ArrayList<DataModal> dataModalArrayList;
    FirebaseFirestore db;
    String type1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        coursesLV = findViewById(R.id.idLVCourses);
        dataModalArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        type1=bundle.getString("type");
        //Toast.makeText(Display.this, type1, Toast.LENGTH_SHORT).show();
        loadDatainListview(type1);
    }
    private void loadDatainListview(String tp) {
        // below line is use to get data from Firebase
        // firestore using collection in android.
        //Toast.makeText(Display.this, type1, Toast.LENGTH_SHORT).show();
        db.collection(tp).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are hiding
                            // our progress bar and adding our data in a list.
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                DataModal dataModal = d.toObject(DataModal.class);

                                // after getting data from Firebase we are
                                // storing that data in our array list
                                dataModalArrayList.add(dataModal);
                            }
                            // after that we are passing our array list to our adapter class.
                            CoursesLVAdapter adapter = new CoursesLVAdapter(Display.this, dataModalArrayList);

                            // after passing this array list to our adapter
                            // class we are setting our adapter to our list view.
                            coursesLV.setAdapter(adapter);


                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(Display.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // we are displaying a toast message
                // when we get any error from Firebase.
                Toast.makeText(Display.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}