package com.example.stockflux;

import java.util.Date;

public class product_view_model_class {
    String product_name;
    Date product_date;
    int product_qty,product_total_price,product_per_price;

    public product_view_model_class(){

    }


    public product_view_model_class(String product_name, Date product_date,int product_per_price, int product_qty, int product_total_price) {
        this.product_name = product_name;
        this.product_date = product_date;
        this.product_per_price = product_per_price;
        this.product_qty = product_qty;
        this.product_total_price = product_total_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Date getProduct_date() {
        return product_date;
    }

    public void setProduct_date(Date product_date) {
        this.product_date = product_date;
    }

    public int getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(int product_qty) {
        this.product_qty = product_qty;
    }

    public int getProduct_total_price() {
        return product_total_price;
    }

    public void setProduct_total_price(int product_total_price) {
        this.product_total_price = product_total_price;
    }

    public int getProduct_per_price() {
        return product_per_price;
    }

    public void setProduct_per_price(int product_per_price) {
        this.product_per_price = product_per_price;
    }

}
