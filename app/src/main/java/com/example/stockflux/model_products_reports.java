package com.example.stockflux;

import java.util.Date;

public class model_products_reports {
    String product_name;
    Date product_date;
    int product_qty;

    public model_products_reports(String product_name, Date product_date, int product_qty) {
        this.product_name = product_name;
        this.product_date = product_date;
        this.product_qty = product_qty;
    }
    public model_products_reports(){ }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(int product_qty) {
        this.product_qty = product_qty;
    }

    public Date getProduct_date() {
        return product_date;
    }

    public void setProduct_date(Date product_date) {
        this.product_date = product_date;
    }

}
