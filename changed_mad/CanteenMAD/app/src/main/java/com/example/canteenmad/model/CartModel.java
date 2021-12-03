package com.example.canteenmad.model;



public class CartModel {
    private String Image;
    private String ItemName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private float Price;

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public float getQuantity() {
        return Quantity;
    }

    public void setQuantity(float quantity) {
        Quantity = quantity;
    }

    private float Quantity;
    private int quan;
    private float totalPrice;
    public CartModel(){

    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }



    public int getQuan() {
        return quan;
    }

    public void setQuan(int quan) {
        this.quan = quan;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
