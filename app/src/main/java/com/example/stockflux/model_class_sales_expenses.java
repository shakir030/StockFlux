package com.example.stockflux;

import java.util.Date;

public class model_class_sales_expenses {
    String Name,Description;
    Date Date;
    int Total_Price;

    public model_class_sales_expenses(){

    }

    public model_class_sales_expenses(String name, Date date, String description, int total_Price) {
        Name = name;
        Date = date;
        Description = description;
        Total_Price = total_Price;
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getTotal_Price() {
        return Total_Price;
    }

    public void setTotal_Price(int total_Price) {
        Total_Price = total_Price;
    }
}
