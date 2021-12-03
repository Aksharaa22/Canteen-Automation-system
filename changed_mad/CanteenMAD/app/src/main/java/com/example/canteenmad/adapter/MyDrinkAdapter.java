package com.example.canteenmad.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.canteenmad.R;
import com.example.canteenmad.eventbus.MyUpdateCartEvent;
import com.example.canteenmad.listener.ICartLoadListener;
import com.example.canteenmad.listener.IRecyclerViewClickListener;
import com.example.canteenmad.model.CartModel;
import com.example.canteenmad.model.DrinkModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyDrinkAdapter extends RecyclerView.Adapter<MyDrinkAdapter.MyDrinkViewHolder> {
    private Context context;
    private List<DrinkModel> drinkModelList;
    private ICartLoadListener iCartLoadListener;

    public MyDrinkAdapter(Context context, List<DrinkModel> drinkModelList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.drinkModelList = drinkModelList;
        this.iCartLoadListener = iCartLoadListener;
    }

    @NonNull
    @Override
    public MyDrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyDrinkViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_drink_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyDrinkViewHolder holder, int position) {
        Glide.with(context)
                .load(drinkModelList.get(position).getImage())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("Rs").append(drinkModelList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder().append(drinkModelList.get(position).getItemName()));
        holder.setListener((view, adapterPosition) -> {
            addTocart(drinkModelList.get(position));
        });
    }

    private void addTocart(DrinkModel drinkModel) {
        String id1= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userCart= FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child(id1);
        userCart.child(drinkModel.getItemName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            
                            CartModel cartModel=snapshot.getValue(CartModel.class);
                            if(cartModel.getQuan()>cartModel.getQuantity()){
                            cartModel.setQuantity(cartModel.getQuantity()+1);
                            Map<String ,Object> updateData=new HashMap<>();
                            updateData.put("quantity",cartModel.getQuantity()+1);
                            updateData.put("totalPrice",cartModel.getQuantity()*cartModel.getPrice());
                            userCart.child(drinkModel.getItemName())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(unused -> {
                                        iCartLoadListener.onCartLoadFailed("Add to cart Success");
                                    })
                                    .addOnFailureListener(e -> {
                                        iCartLoadListener.onCartLoadFailed(e.getMessage());
                                    });}
                            else
                            {
                                Toast.makeText(context.getApplicationContext(), "Insufficient Quantity", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{

                            CartModel cartModel=new CartModel() ;
                            cartModel.setItemName(drinkModel.getItemName());
                            cartModel.setImage(drinkModel.getImage());
                            cartModel.setPrice(drinkModel.getPrice());
                            cartModel.setQuan(drinkModel.getQuantity());
                            cartModel.setQuantity(1);
                            cartModel.setTotalPrice(drinkModel.getPrice());
                            cartModel.setType(drinkModel.getType());

                            userCart.child(drinkModel.getItemName())
                                    .setValue(cartModel)
                                    .addOnSuccessListener(unused -> {
                                        iCartLoadListener.onCartLoadFailed("Add to cart Success");
                                    })
                                    .addOnFailureListener(e -> {
                                        iCartLoadListener.onCartLoadFailed(e.getMessage());
                                    });

                        }
                        EventBus.getDefault().postSticky(new MyUpdateCartEvent());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iCartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return drinkModelList.size();
    }

    public class MyDrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtPrice)
        TextView txtPrice;

        IRecyclerViewClickListener listener;
        public  void setListener(IRecyclerViewClickListener listener){
            this.listener=listener;
        }

        private Unbinder unbinder;

        public MyDrinkViewHolder(View itemView){
            super(itemView);
            unbinder= ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listener.onRecyclerClick(view,getAdapterPosition());
        }
    }
}