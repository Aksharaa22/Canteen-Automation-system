package com.example.canteenmad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canteenmad.adapter.MyCartAdapter;
import com.example.canteenmad.eventbus.MyUpdateCartEvent;
import com.example.canteenmad.listener.ICartLoadListener;
import com.example.canteenmad.model.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements ICartLoadListener {

double sum1;
    @BindView(R.id.recycler_cart)
    RecyclerView recyclerCart;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    Button btpay;
    FirebaseFirestore fb,fb1,db;
     int balance1;
    ICartLoadListener cartLoadListener;

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

        loadCartFromFirebase();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        btpay=findViewById(R.id.button2);
        btpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id1= FirebaseAuth.getInstance().getCurrentUser().getUid();
                String id2= FirebaseAuth.getInstance().getCurrentUser().getEmail();
                fb=FirebaseFirestore.getInstance();
                DocumentReference doc=fb.collection("User").document(id1);
                doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            DocumentSnapshot fk=task.getResult();
                            if(fk.exists()){
                                double b=fk.getDouble("balance");
                                if(b>sum1)
                                {
                                    int max=100000,min=4000;
                                    int bill1 = (int)(Math.random()*(max-min+1)+min);
                                    Toast.makeText(CartActivity.this, "Payed.The bill number is Mailed ", Toast.LENGTH_SHORT).show();
                                    b=b-sum1;
                                    doc.update("balance", b);
                                    String mMessage = "Your Bill Number is "+ bill1+ " Collect it after 15 minutes.\n  Thank You. Have a nice Day";
                                    String mSubject="PSG Canteen";
                                    JavaMailAPI javaMailAPI = new JavaMailAPI(getApplicationContext(),id2 , mSubject, mMessage);
                                    javaMailAPI.execute();
                                   // FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    //DatabaseReference myRef = database.getReference("Cart").child(id1);
                                    //CartModel cartModel=new CartModel(myRef);
                                    FirebaseDatabase.getInstance()
                                            .getReference("Cart")
                                            .child(id1)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists())
                                                    {
                                                        for(DataSnapshot cartSnapshot:snapshot.getChildren())
                                                        {
                                                            CartModel cartModel=cartSnapshot.getValue(CartModel.class);
                                                            cartModel.setItemName(cartSnapshot.getKey());
                                                          //  Toast.makeText(CartActivity.this,cartModel.getItemName(),Toast.LENGTH_SHORT).show();
                                                            String itemname,types;
                                                            float r;
                                                            types=cartModel.getType();
                                                            //Toast.makeText(CartActivity.this,types,Toast.LENGTH_SHORT).show();
                                                            itemname=cartModel.getItemName();
                                                            r=cartModel.getQuantity();
                                                            fb1=FirebaseFirestore.getInstance();
                                                            DocumentReference doc=fb1.collection(types).document(itemname);
                                                            doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @RequiresApi(api = Build.VERSION_CODES.O)
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if(task.isSuccessful()){
                                                                        DocumentSnapshot fk=task.getResult();
                                                                        if(fk.exists()){
                                                                            double t=fk.getDouble("Quantity");
                                                                            t=t-r;
                                                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                                            Calendar c = Calendar.getInstance();
                                                                            String date = sdf.format(c.getTime());
                                                                            doc.update("Quantity",t);
                                                                            db= FirebaseFirestore.getInstance();
                                                                            HashMap<String , Object> map=new HashMap<>();
                                                                            map.put("ItemName",cartModel.getItemName());
                                                                            map.put("Type",cartModel.getType());
                                                                            map.put("Quantity",cartModel.getQuantity());
                                                                            map.put("Date",date);
                                                                            map.put("Price",cartModel.getPrice());
                                                                            map.put("timestamp",java.time.LocalTime.now());
                                                                            String x=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                                                            map.put("Email",x);
                                                                            HistoryModel models=new HistoryModel(cartModel.getItemName(),cartModel.getType(),date,cartModel.getQuantity(),cartModel.getPrice(),x);
                                                                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
                                                                            LocalDateTime now = LocalDateTime.now();
                                                                            FirebaseDatabase dd=FirebaseDatabase.getInstance();
                                                                             DatabaseReference root=dd.getReference().child("Bill").child(String.valueOf(bill1)).child(cartModel.getItemName());
                                                                             root.updateChildren(map);
                                                                             DatabaseReference root11=dd.getReference().child("Bill_No");
                                                                            HashMap<String , Object> map1=new HashMap<>();
                                                                            map1.put("Bill",String.valueOf(bill1));
                                                                            root11.updateChildren(map1);
                                                                            DatabaseReference root1=dd.getReference().child("Student_Bill").child(id1).child(String.valueOf(bill1)).child(cartModel.getItemName());
                                                                             root1.updateChildren(map);
                                                                            FirebaseDatabase.getInstance()
                                                                                    .getReference("Cart")
                                                                                    .child(id1)
                                                                                    .child(cartModel.getItemName())
                                                                                    .removeValue()
                                                                                    .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
                                                                        }

                                                    }}});
                                                        }}
                                                    else{
                                                        cartLoadListener.onCartLoadFailed("Cart empty");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    cartLoadListener.onCartLoadFailed(error.getMessage());
                                                }
                                            });


                                }
                                else
                                {
                                    Toast.makeText(CartActivity.this, "Balance is not enough!! ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
              /*  doc.addSnapshotListener(CartActivity.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        double b=value.getDouble("balance");

                        if(b>sum1)
                        {
                            Toast.makeText(CartActivity.this, "Payed", Toast.LENGTH_SHORT).show();
                           b=b-sum1;

                            doc.update("balance", b);
                        }
                        else
                        {
                            Toast.makeText(CartActivity.this, "not Payed ", Toast.LENGTH_SHORT).show();
                        }
                        loadCartFromFirebase();
                    }
                });*/
            }
        });
        loadCartFromFirebase();

    }

    private void loadCartFromFirebase() {
        String id1= FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<CartModel> cartModels=new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(id1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot cartSnapshot:snapshot.getChildren())
                            {
                                CartModel cartModel=cartSnapshot.getValue(CartModel.class);
                                cartModel.setItemName(cartSnapshot.getKey());
                                cartModels.add(cartModel);
                            }
                            cartLoadListener.onCartLoadSuccess(cartModels);
                        }
                        else{
                            cartLoadListener.onCartLoadFailed("Cart empty");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    private  void init(){
        ButterKnife.bind(this);
        cartLoadListener=this;
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerCart.setLayoutManager(layoutManager);
        recyclerCart.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));
        btnBack.setOnClickListener(v->finish());
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
         sum1=0;
        for(CartModel cartModel:cartModelList){
            sum1+=cartModel.getTotalPrice();
        }
        txtTotal.setText(new StringBuilder("Rs. ").append(sum1));
        MyCartAdapter adapter=new MyCartAdapter(this,cartModelList);
        recyclerCart.setAdapter(adapter);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout,message,Snackbar.LENGTH_LONG).show();
    }
}