package com.example.canteenmad.utils;

import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(parent.getChildLayoutPosition(view)%2!=0)
        {
            outRect.top=50;
            outRect.bottom=-50;
        }
    }


}
