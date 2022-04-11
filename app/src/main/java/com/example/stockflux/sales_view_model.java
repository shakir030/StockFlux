package com.example.stockflux;

import java.util.Date;

public class sales_view_model{
    String name,id,description,purchase_ID;
    Date date;
    int quantity,per_price,total_Price;

    public sales_view_model(){

    }
    public sales_view_model(String name, String id, Date date, String description, String purchase_ID, int quantity, int per_price, int total_price) {
        this.name = name;
        this.id = id;
        this.date = date;
        this.description = description;
        this.purchase_ID = purchase_ID;
        this.quantity = quantity;
        this.per_price = per_price;
        this.total_Price = total_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPurchase_ID() {
        return purchase_ID;
    }

    public void setPurchase_ID(String purchase_ID) {
        this.purchase_ID = purchase_ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPer_price() {
        return per_price;
    }

    public void setPer_price(int per_price) {
        this.per_price = per_price;
    }


    public int getTotal_price() {
        return total_Price=quantity*per_price;
    }

    public void setTotal_price(int total_price) {
        this.total_Price = total_price;
    }

}
