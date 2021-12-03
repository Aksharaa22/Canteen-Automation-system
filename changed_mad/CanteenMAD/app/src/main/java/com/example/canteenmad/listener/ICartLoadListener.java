package com.example.canteenmad.listener;

import com.example.canteenmad.model.CartModel;
import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailed(String message);
}
