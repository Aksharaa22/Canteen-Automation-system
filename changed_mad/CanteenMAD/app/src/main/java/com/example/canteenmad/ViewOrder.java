package com.example.canteenmad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canteenmad.eventbus.MyUpdateCartEvent;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

public class ViewOrder extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecycerView;
    private ImageAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    List<HistoryModel> muploads;
    Button ready;
    String mEmail ;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    String x;
    FirebaseAuth mauth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Bundle p = getIntent().getExtras();
        x = p.getString("billno");
        ready=findViewById(R.id.button3);
        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.nav_view);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        //getSupportActionBar().setLogo(R.drawable.hamburger_icon);
        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.userDisplayName);
        TextView userEmail = headerView.findViewById(R.id.userDisplayEmail);


        userEmail.setText("Admin");
        //username.setText(user.getDisplayName());

        ready.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {

                                         String mMessage = "Your Order has ready. Come and collect it!!!\n Thank You. Have a nice Day";
                                         String mSubject="Canteen";
                                         JavaMailAPI javaMailAPI = new JavaMailAPI(getApplicationContext(), mEmail, mSubject, mMessage);
                                         javaMailAPI.execute();
                                         FirebaseDatabase.getInstance()
                                                 .getReference("Bill")
                                                 .child(x)
                                                 .removeValue()
                                                 .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
                                         Intent t=new Intent(ViewOrder.this,BillDisplay.class);
                                         startActivity(t);

                                     }
                                 });
//ready.setOnClickListener(new onClickListener);
                mRecycerView = findViewById(R.id.recycler_view);
        mRecycerView.setHasFixedSize(true);
        mRecycerView.setLayoutManager(new LinearLayoutManager(this));

        muploads= new ArrayList<>();
        //muploads.clear();
           String uid = user.getUid();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Bill");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                muploads.clear();
String em;
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    //System.out.println(postSnapshot.child("Quantity").get());
                    String t=postSnapshot.getKey();
                    //String tt="35510";
                    //Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT).show();
                    if(t.equals(x)){
                        for(DataSnapshot d1:postSnapshot.getChildren())
                        {
                            HistoryModel upload=d1.getValue((HistoryModel.class));
                            muploads.add(upload);
                            System.out.print(upload.getItemName());
                            mEmail=upload.getEmail();
                            //Toast.makeText(getApplicationContext(),p,Toast.LENGTH_SHORT).show();

                            HashMap<String , Object> map=new HashMap<>();
                            map.put("ItemName",upload.getItemName());
                            map.put("Type",upload.getType());
                            map.put("Quantity",upload.getQuantity());
                            map.put("Date",upload.getDate());
                            map.put("Price",upload.getPrice());
                            map.put("Email",upload.getEmail());
//  map.put("timestamp",d1.child("timestamp"));

                            FirebaseDatabase dd=FirebaseDatabase.getInstance();
                            DatabaseReference root=dd.getReference().child("PendingOrder").child(x).child(upload.getItemName());
                            root.updateChildren(map);


                        }
                    }
                    //Toast.makeText(getApplicationContext(),upload.getItemName(),Toast.LENGTH_SHORT).show();
                }
                mAdapter=new ImageAdapter(ViewOrder.this,muploads);
                mRecycerView.setAdapter(mAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewOrder.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId()){
            case R.id.ViewOrder:
                startActivity(new Intent(this,BillDisplay.class));
                //overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                break;
            case R.id.AddMenu:
                startActivity(new Intent(this,MenuAdd.class));
                //overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                break;
            case R.id.ModifyMenu:
                Intent i=new Intent(this,Modify_Home_Java.class);
                i.putExtra("email",user.getEmail());
                //startActivity(new Intent(this,DownloadPDF.class));
                startActivity(i);

                // overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                break;

            case R.id.PendingOrder:
                Intent i1=new Intent(this,PendingOrder.class);
                i1.putExtra("email",user.getEmail());
                //startActivity(new Intent(this,DownloadPDF.class));
                startActivity(i1);
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