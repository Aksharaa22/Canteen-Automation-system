package com.example.canteenmad;

public class DataModal {
    String ItemName,Price,Quantity;
    public DataModal(){

    }

    public DataModal(String itemName, String price, String quantity) {
        ItemName = itemName;
        Price = price;
        Quantity = quantity;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}