package com.example.canteenmad;

public class HistoryModel {
    String ItemName;
    String Type;
    String Date;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    String Email;
    float Quantity;

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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public float getQuantity() {
        return Quantity;
    }

    public void setQuantity(float quantity) {
        Quantity = quantity;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    float Price;

    public HistoryModel(String itemName, String type, String date, float quantity, float price,String email) {
        ItemName = itemName;
        Type = type;
        Date = date;
        Quantity = quantity;
        Price = price;
        Email=email;

    }
    public  HistoryModel(){

    }
}
