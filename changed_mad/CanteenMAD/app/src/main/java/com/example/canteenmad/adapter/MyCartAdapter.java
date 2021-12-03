package com.example.canteenmad.adapter;




import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.canteenmad.model.CartModel;
import com.example.canteenmad.model.DrinkModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {


    private Context context;
    private List<CartModel> cartModelList;

    public MyCartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCartViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, int position) {
        Glide.with(context)
                .load(cartModelList.get(position).getImage())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("Rs. ").append(cartModelList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder().append(cartModelList.get(position).getItemName()));
        holder.txtQuantity.setText(new StringBuilder().append(cartModelList.get(position).getQuantity()));

        holder.btnMinus.setOnClickListener(v->{
            minusCartItem(holder,cartModelList.get(position));
        });
        holder.btnPlus.setOnClickListener(v->{
            plusCartItem(holder,cartModelList.get(position));
        });

        holder.btnDelete.setOnClickListener(v->{
            AlertDialog dialog=new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Do you really want to delete item")
                    .setNegativeButton("Cancel",(dialog1,which)->dialog1.dismiss())
                    .setPositiveButton("Ok",(dialog2,which)->{

                        notifyItemRemoved(position);

                        deleteFromFirebase(cartModelList.get(position));
                        dialog2.dismiss();
                    }).create();
            dialog.show();
        });

    }

    private void deleteFromFirebase(CartModel cartModel) {

        String id1= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(id1)
                .child(cartModel.getItemName())
                .removeValue()
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

    private void plusCartItem(MyCartViewHolder holder, CartModel cartModel) {

        if(cartModel.getQuantity() < (cartModel.getQuan())){
        cartModel.setQuantity(cartModel.getQuantity()+1);
        cartModel.setTotalPrice(cartModel.getQuantity()*(cartModel.getPrice()));

        holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
        updateFirebase(cartModel);}
        else
        {
            Toast.makeText(context.getApplicationContext(), "Insufficient Quantity",Toast.LENGTH_SHORT).show();
        }
    }

    private void minusCartItem(MyCartViewHolder holder, CartModel cartModel) {
        if (cartModel.getQuantity()>1){
            cartModel.setQuantity(cartModel.getQuantity()-1);
            cartModel.setTotalPrice(cartModel.getQuantity()*(cartModel.getPrice()));

            holder.txtQuantity.setText(new StringBuilder().append(cartModel.getQuantity()));
            updateFirebase(cartModel);
        }
    }

    private void updateFirebase(CartModel cartModel) {
        String id1= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(id1)
                .child(cartModel.getItemName())
                .setValue(cartModel)
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class MyCartViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.btnMinus)
        ImageView btnMinus;
        @BindView(R.id.btnPlus)
        ImageView btnPlus;
        @BindView(R.id.btnDelete)
        ImageView btnDelete;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtPrice)
        TextView txtPrice;
        @BindView(R.id.txtQuantity)
        TextView txtQuantity;

        Unbinder unbinder;

        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder= ButterKnife.bind(this,itemView);
        }
    }
}