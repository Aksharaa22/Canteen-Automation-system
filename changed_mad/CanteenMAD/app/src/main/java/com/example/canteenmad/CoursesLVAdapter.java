package com.example.canteenmad;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class CoursesLVAdapter extends ArrayAdapter<DataModal> {
    public CoursesLVAdapter(@NonNull Context context, ArrayList<DataModal> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // below line is use to inflate the
        // layout for our item of list view.
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.image_lv_item, parent, false);
        }
        DataModal dataModal = getItem(position);
        TextView ItemName = listitemView.findViewById(R.id.idTVtext1);
        TextView Price = listitemView.findViewById(R.id.idTVtext2);
        TextView Quantity=listitemView.findViewById(R.id.idTVtext3);
        Button sub=listitemView.findViewById(R.id.sub);
        Button addi=listitemView.findViewById(R.id.addi);
        EditText show=listitemView.findViewById(R.id.show);
        show.setText("0");
        sub.setEnabled(false);
        show.setEnabled(false);
        ItemName.setText(dataModal.getItemName());
        Price.setText(dataModal.getPrice());
        Quantity.setText(dataModal.getQuantity());
        final Integer[] quan = {new Integer(dataModal.getQuantity())};
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a= (show.getText().toString());
                Integer y=new Integer(a);
                y=y-1;
                if(y==0)
                    sub.setEnabled(false);
                show.setText(Integer.toString(y));
                addi.setEnabled(true);
            }
        });
        addi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a= (show.getText().toString());

                // String o="10";
                // int in2 = new Integer(o);
                Integer y=new Integer(a);
                y=y+1;
                //String f=in2;

                show.setText(Integer.toString(y));
                //   quan[0] = quan[0] -1;
                //   Quantity.setText(Integer.toString(quan[0]));

                if(quan[0]<=0){
                    quan[0]=0;
                    addi.setEnabled(false);}
                sub.setEnabled(true);
                //Toast.makeText(getContext(),o,Toast.LENGTH_SHORT).show();
            }
        });
        return listitemView;
    }
}

