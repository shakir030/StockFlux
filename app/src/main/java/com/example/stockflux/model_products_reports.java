package com.example.stockflux;

public class model_products_reports {
    String product_name;
    int product_qty;

    public model_products_reports(){ }

    public model_products_reports(String product_name, int product_qty) {
        this.product_name = product_name;
        this.product_qty = product_qty;
    }

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

}
