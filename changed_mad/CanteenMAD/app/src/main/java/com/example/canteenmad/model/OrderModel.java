package com.example.canteenmad.model;

public class OrderModel {
    String Date,ItemName,Type;
    int Price,Quantity;

    public OrderModel(String date, String itemName, String type, int price, int quantity) {
        Date = date;
        ItemName = itemName;
        Type = type;
        Price = price;
        Quantity = quantity;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
