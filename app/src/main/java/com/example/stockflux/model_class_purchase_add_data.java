package com.example.stockflux;

import java.util.Date;

public class model_class_purchase_add_data {
     String product_name,product_id,product_description;
     Date product_date;
     int product_qty,product_per_price,product_total_price;

    public model_class_purchase_add_data(){

    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public Date getProduct_date() {
        return product_date;
    }

    public void setProduct_date(Date product_date) {
        this.product_date = product_date;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public int getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(int product_qty) {
        this.product_qty = product_qty;
    }

    public int getProduct_per_price() {
        return product_per_price;
    }

    public void setProduct_per_price(int product_per_price) {
        this.product_per_price = product_per_price;
    }

    public int getProduct_total_price() {
        return product_qty*product_per_price;
    }

    public void setProduct_total_price(int product_total_price) {
        this.product_total_price =  product_total_price;
    }
}
