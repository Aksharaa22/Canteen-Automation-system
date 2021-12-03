package com.example.canteenmad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MenuAdd extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
TextView t;
StorageReference storageReference;
EditText name,price,quan;
Button additem;
Spinner s;
FirebaseFirestore db;
Uri imageUri,b;
String item;
String types;
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
        setContentView(R.layout.activity_menu_add);
        storageReference = FirebaseStorage.getInstance().getReference();
        db= FirebaseFirestore.getInstance();
        t=findViewById(R.id.textView2);
        additem=findViewById(R.id.Add);
        name = findViewById(R.id.Itemname);
        price = findViewById(R.id.Price);
        quan = findViewById(R.id.Quantity);
        s = findViewById(R.id.spinner);
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

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
selectImage();
            }


        });
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                item = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        List<String> ex = new ArrayList<String>();
        ex.add("Breakfast");
        ex.add("Lunch");
        ex.add("Beverage");
        ex.add("Evening Tiffen");
        ex.add("Snacks");

        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, ex);

         s.setAdapter(data);

        additem.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         
                         savef(name.getText().toString(),item,price.getText().toString(),quan.getText().toString(),b);
                     }

                     private void savef(String name,String type, String price, String quan,Uri data)
                     {


                         SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
                         Date now = new Date();

                         String fileName = formatter.format(now);
                         storageReference = FirebaseStorage.getInstance().getReference();
                         StorageReference reference=storageReference.child(fileName);

                         reference.putFile(imageUri)
                                 .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                     @Override
                                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                         reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                             @Override
                                             public void onSuccess(Uri uri) {

                                                     HashMap<String , Object> map=new HashMap<>();
                                                     map.put("ItemName",name);
                                                     map.put("type",type);
                                                     map.put("Price",Integer.parseInt((price)));
                                                     map.put("Quantity",Integer.parseInt(quan));
                                                     map.put("Image",uri.toString());
                                                 db.collection(type).document(name).set(map)
                                                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {
                                                                 Toast.makeText(MenuAdd.this,"Item added",Toast.LENGTH_SHORT).show();
                                                                 startActivity(new Intent(MenuAdd.this,BillDisplay.class));
                                                             }
                                                         }).addOnFailureListener(new OnFailureListener() {
                                                     @Override
                                                     public void onFailure(@NonNull Exception e) {
                                                         Toast.makeText(MenuAdd.this,"Item not added",Toast.LENGTH_SHORT).show();
                                                     }
                                                 });

                                             }
                                         });
                                     }});


                         StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                         StorageReference dateRef = storageRef.child(fileName);
                         dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                         {
                             @Override
                             public void onSuccess(Uri downloadUrl)
                             {

                                 Toast.makeText(MenuAdd.this,downloadUrl+" pop",Toast.LENGTH_SHORT).show();

                             }
                         });

                     }
        });


    }
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,  100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null){
            imageUri  = data.getData();

            b=data.getData();
            t.setText("Image is Selected");

        }
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
                Intent i=new Intent(this,ModifyHome.class);
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