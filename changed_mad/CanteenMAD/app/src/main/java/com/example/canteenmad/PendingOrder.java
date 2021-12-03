package com.example.canteenmad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
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

import com.example.canteenmad.eventbus.MyUpdateCartEvent;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PendingOrder extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView;

    List<String> uploadedpdf;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    String x;
    FirebaseAuth mauth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);
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

        uploadedpdf=new ArrayList<String>();
        listView = (ListView) findViewById(R.id.listview);
        retrivebillno();



    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo i=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        String putPDF=uploadedpdf.get(i.position);
        final String[] mEmail = new String[1];
        final String[] t = new String[1];
        final String[] em = new String[1];
        switch (item.getItemId()){
            case R.id.mail:
                //Toast.makeText(PendingOrder.this,putPDF,Toast.LENGTH_SHORT).show();
                mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("PendingOrder");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for(DataSnapshot postSnapshot : snapshot.getChildren()){
                            //System.out.println(postSnapshot.child("Quantity").get());
                            t[0] =postSnapshot.getKey();
                            //String tt="35510";
                            //Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT).show();
                            if(t[0].equals(putPDF)){
                                for(DataSnapshot d1:postSnapshot.getChildren())
                                {
                                    HistoryModel upload=d1.getValue((HistoryModel.class));
                                    //muploads.add(upload);
                                    //System.out.print(upload.getItemName());
                                    mEmail[0] =upload.getEmail();
                                    String mMessage = "Bill Number: "+ t[0] +"\nYour Order has been ready already. Please Come and get it.Otherwise your order will be cancelled. Money will not be refunded.\n Thank You. Have a nice Day";
                                    String mSubject="Remainding  your order Canteen";
                                    JavaMailAPI javaMailAPI = new JavaMailAPI(PendingOrder.this, mEmail[0], mSubject, mMessage);
                                    javaMailAPI.execute();
                                    //Toast.makeText(getApplicationContext(),mEmail[0],Toast.LENGTH_SHORT).show();

                                }
                            }
                            //Toast.makeText(getApplicationContext(),upload.getItemName(),Toast.LENGTH_SHORT).show();
                        }



                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PendingOrder.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

                return true;
            case R.id.got:
                mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("PendingOrder");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for(DataSnapshot postSnapshot : snapshot.getChildren()){
                            //System.out.println(postSnapshot.child("Quantity").get());
                            t[0] =postSnapshot.getKey();
                            //String tt="35510";
                            //Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT).show();
                            if(t[0].equals(putPDF)){
                                for(DataSnapshot d1:postSnapshot.getChildren())
                                {
                                    HistoryModel upload=d1.getValue((HistoryModel.class));
                                    //muploads.add(upload);
                                    //System.out.print(upload.getItemName());
                                    mEmail[0] =upload.getEmail();
                                    String mMessage = "Bill Number: "+ t[0] +"\nYou recieved your Order.\n Thank You. Have a nice Day";
                                    String mSubject="Canteen";
                                    JavaMailAPI javaMailAPI = new JavaMailAPI(PendingOrder.this, mEmail[0], mSubject, mMessage);
                                    javaMailAPI.execute();
                                    //Toast.makeText(getApplicationContext(),mEmail[0],Toast.LENGTH_SHORT).show();
                                    FirebaseDatabase.getInstance()
                                            .getReference("PendingOrder")
                                            .child(t[0])
                                            .removeValue()
                                            .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
                                }
                            }
                            //Toast.makeText(getApplicationContext(),upload.getItemName(),Toast.LENGTH_SHORT).show();
                        }



                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PendingOrder.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });


            case R.id.cancel:
                mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("PendingOrder");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for(DataSnapshot postSnapshot : snapshot.getChildren()){
                            //System.out.println(postSnapshot.child("Quantity").get());
                            t[0] =postSnapshot.getKey();
                            //String tt="35510";
                            //Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT).show();
                            if(t[0].equals(putPDF)){
                                for(DataSnapshot d1:postSnapshot.getChildren())
                                {
                                    HistoryModel upload=d1.getValue((HistoryModel.class));
                                    //muploads.add(upload);
                                    //System.out.print(upload.getItemName());
                                    mEmail[0] =upload.getEmail();
                                    String mMessage = "Bill Number: "+ t[0] +"\nYour Order has been Cancelled.Money is not refunded.\n Thank You. Have a nice Day";
                                    String mSubject="Remainding  your order Canteen";
                                    JavaMailAPI javaMailAPI = new JavaMailAPI(PendingOrder.this, mEmail[0], mSubject, mMessage);
                                    javaMailAPI.execute();
                                    //Toast.makeText(getApplicationContext(),mEmail[0],Toast.LENGTH_SHORT).show();
                                    FirebaseDatabase.getInstance()
                                            .getReference("PendingOrder")
                                            .child(t[0])
                                            .removeValue()
                                            .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
                                }
                            }
                            //Toast.makeText(getApplicationContext(),upload.getItemName(),Toast.LENGTH_SHORT).show();
                        }



                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PendingOrder.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });


                return true;

            default:
                return super.onContextItemSelected(item);

        }

    }



    private void retrivebillno() {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("PendingOrder");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadedpdf.clear();
                for(DataSnapshot ss:snapshot.getChildren())
                {
                    uploadedpdf.add(ss.getKey().toString());
                    //System.out.print(ss.getKey().toString());
                    for(DataSnapshot d1:ss.getChildren())
                    {
                        String itn=d1.child("ItemName").getValue().toString();
                        //System.out.println(itn);
                    }
                    //System.out.print("uploadedpdf");
                    //System.out.print(uploadedpdf);
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




                listView.setAdapter(arrayAdapter);
                registerForContextMenu(listView);
                arrayAdapter.notifyDataSetChanged();
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