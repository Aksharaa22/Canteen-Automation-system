package com.example.canteenmad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canteenmad.adapter.MyCartAdapter;
import com.example.canteenmad.adapter.MyDrinkAdapter;
import com.example.canteenmad.eventbus.MyUpdateCartEvent;
import com.example.canteenmad.listener.ICartLoadListener;
import com.example.canteenmad.listener.IDrinkLoadListener;
import com.example.canteenmad.model.CartModel;
import com.example.canteenmad.model.DrinkModel;
import com.example.canteenmad.utils.SpaceItemDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DisplayMenuCart<OnResume> extends AppCompatActivity implements IDrinkLoadListener, ICartLoadListener ,NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.recycler_drink)
    RecyclerView recyclerDrink;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    @BindView(R.id.badge)
    NotificationBadge badge;
    @BindView(R.id.btnCart)
    FrameLayout btnCart;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    String x;
    FirebaseAuth mauth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    IDrinkLoadListener drinkLoadListener;
    ICartLoadListener cartLoadListener;
String types,id1;
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event)
    {
        countCartItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu_cart);
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
                Toast.makeText(DisplayMenuCart.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        userEmail.setText(user.getEmail());

        //Toast.makeText(getApplicationContext(), bal[0], Toast.LENGTH_SHORT).show();

        Bundle bundle = getIntent().getExtras();
        types=bundle.getString("type");
        id1=bundle.getString("id");
        init();
        loadDrinkFromFirebase();
        countCartItem();
    }

    private void loadDrinkFromFirebase() {
        List<DrinkModel> drinkModels=new ArrayList<>();
        FirebaseFirestore.getInstance()
                .collection(types)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                DrinkModel drinkModal = d.toObject(DrinkModel.class);
                                //drinkModal.setItemName(d.getItemName);
                                drinkModels.add(drinkModal);
                            }
                            drinkLoadListener.onDrinkLoadSuccess(drinkModels);
                        }
                        else{
                            drinkLoadListener.onDrinkLoadFailed("Can't find Menu");
                        }

                    }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // we are displaying a toast message
                // when we get any error from Firebase.
                Toast.makeText(DisplayMenuCart.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init()
    {
        ButterKnife.bind(this);
        drinkLoadListener=this;
        cartLoadListener=this;
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerDrink.setLayoutManager(gridLayoutManager);
        recyclerDrink.addItemDecoration(new SpaceItemDecoration());

        btnCart.setOnClickListener(v->startActivity(new Intent(this,CartActivity.class)));
    }

    @Override
    public void onDrinkLoadSuccess(List<DrinkModel> drinkModelList) {
        MyDrinkAdapter adapter=new MyDrinkAdapter(this,drinkModelList,cartLoadListener);
        recyclerDrink.setAdapter(adapter);
    }

    @Override
    public void onDrinkLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }

    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        int cartSum=0;
        for(CartModel cartModel:cartModelList)
            cartSum+=cartModel.getQuantity();
        badge.setNumber(cartSum);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }
    @Override
    protected void onResume() {

        super.onResume();
        countCartItem();
    }

    private void countCartItem() {
        List<CartModel> cartModels=new ArrayList<>();
        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child(id1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot cartSnapshot:snapshot.getChildren())
                        {
                            CartModel cartModel=cartSnapshot.getValue(CartModel.class);
                            cartModel.setItemName(cartSnapshot.getKey());
                            cartModels.add(cartModel);
                        }
                        cartLoadListener.onCartLoadSuccess(cartModels);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });

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
                startActivity(new Intent(this,Dashboard.class));
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