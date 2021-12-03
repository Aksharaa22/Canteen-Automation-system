package com.example.canteenmad;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;

    private List<HistoryModel> muploads;
    //private OnItemClickListner mListner;
    public ImageAdapter(Context context,List<HistoryModel> uploads){
        muploads=uploads;
        mContext=context;

    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        HistoryModel uploadCurrent=muploads.get(position);
        holder.textViewName.setText(uploadCurrent.getItemName());
        String p= String.valueOf(uploadCurrent.getQuantity());
        holder.textViewQuantity.setText(p);
    }

    @Override
    public int getItemCount() {
        return muploads.size();
    }

    public  class ImageViewHolder extends  RecyclerView.ViewHolder {
        public TextView textViewName,textViewQuantity;
        public ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName=itemView.findViewById(R.id.text_view_name);
            textViewQuantity=itemView.findViewById(R.id.text_view_quan);

        }




    }}

