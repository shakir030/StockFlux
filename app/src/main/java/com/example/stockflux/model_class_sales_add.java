package com.example.stockflux;

public class model_class_sales_add {
    String Name,ID,Date,Description;
    int Quantity,Total_Price,per_price;

    model_class_sales_add(){

    }

    public String getName() {
        return Name;
    }



    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDate() {
        return Date;
    }



    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getTotal_Price() {
        Total_Price = Quantity * per_price;
        return Total_Price;
    }

    public void setTotal_Price(int total_Price) {
        Total_Price = total_Price;
    }
    public void setDate(String date) {
        Date = date;
    }
    public int getPer_price() {
        return per_price;
    }

    public void setPer_price(int per_price) {
        this.per_price = per_price;
    }
}
