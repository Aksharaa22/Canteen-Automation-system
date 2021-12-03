package com.example.canteenmad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canteenmad.adapter.MyCartAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Student_bill_no extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView;
    DatabaseReference databaseReference;
    List<String> uploadedpdf;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    String x;
    FirebaseAuth mauth;
    final String[] bal = new String[1];
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_bill_no);

        //toolbar=findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.nav_view);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        final String[] bal = new String[1];
        //getSupportActionBar().setLogo(R.drawable.hamburger_icon);
        final View[] headerView = {navigationView.getHeaderView(0)};
        TextView username = headerView[0].findViewById(R.id.userDisplayName);
        TextView userEmail = headerView[0].findViewById(R.id.userDisplayEmail);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("User").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Double t=task.getResult().getDouble("balance");
                System.out.print(t);
                bal[0] =String.valueOf(t);
            }
        });
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(DocumentSnapshot snapshot: task.getResult()){
                            //list.add(snapshot.getId() );
                            String t=snapshot.getId();
                            if(t.equals(user.getUid())) {
                                double rr= snapshot.getDouble("balance");
                                String r=String.valueOf(rr);
                                username.setText(r);

                            }


                        }



                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Student_bill_no.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        userEmail.setText(user.getEmail());

        //Toast.makeText(getApplicationContext(), bal[0], Toast.LENGTH_SHORT).show();
        uploadedpdf=new ArrayList<String>();
        listView = (ListView) findViewById(R.id.listview);
        retrivebillno();



    }

    private void retrivebillno() {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Student_Bill").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ss:snapshot.getChildren())
                {
                    uploadedpdf.add(ss.getKey().toString());
                    System.out.print(ss.getKey().toString());
                    for(DataSnapshot d1:ss.getChildren())
                    {
                        //String itn=d1.child("ItemName").getValue().toString();
                        //System.out.println(itn);
                    }
                    System.out.print("uploadedpdf");
                    System.out.print(uploadedpdf);
                }
                String[] uploadsName=new String[uploadedpdf.size()];
                for(int i=0;i<uploadsName.length;i++){
                    uploadsName[i]=uploadedpdf.get(i);
                }
                final List<String> ListElementsArrayList = new ArrayList<>(Arrays.asList(uploadsName));
                //     ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,ListElementsArrayList);
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,uploadedpdf){
                    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent)
                    {
                        View view=super.getView(position,convertView,parent);
                        TextView textView=(TextView) view.findViewById(android.R.id.text1);
                        //textView.setTextColor(000000);
                        textView.setTextSize(20);
                        return view;
                    }
                };
               /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = (String) parent.getItemAtPosition(position);
                        //Toast.makeText(BillDisplay.this,selectedItem,Toast.LENGTH_SHORT).show();
                        Intent n1=new Intent(Student_bill_no.this,ViewOrder.class);
                        n1.putExtra("billno",selectedItem);
                        startActivity(n1);
                    }
                });*/


                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
       /*
        /*ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, uploadedpdf);
        /*
        listView.setAdapter(itemsAdapter);*/
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId()){
            case R.id.history:
                startActivity(new Intent(this,Student_bill_no.class));
                //overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                break;
            case R.id.emenu:
                startActivity(new Intent(this,Dashingboard.class));
                //overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                break;
            case R.id.cart:
                Intent i=new Intent(this, MyCartAdapter.class);
                i.putExtra("email",user.getEmail());
                //startActivity(new Intent(this,DownloadPDF.class));
                startActivity(i);

                // overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                break;


            case R.id.logout:
                checkUser();
                break;

            default:
                Toast.makeText(this, "Coming soon.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

//  public boolean onNavigationItemSelected(@NonNull MenuItem item){

//}

    private void checkUser() {
        // if user is real or not

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        //overridePendingTransition(R.anim.slide_up,R.anim.slide_down);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        return super.onCreateOptionsMenu(menu);
    }


}