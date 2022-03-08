package com.example.stockflux;

public class model_profit_and_loss{
    String product_name;
    int product_total_price;

    public model_profit_and_loss(){ }

    public model_profit_and_loss(String product_name, int product_total_price) {
        this.product_name = product_name;
        this.product_total_price = product_total_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_total_price() {
        return product_total_price;
    }

    public void setProduct_total_price(int product_total_price) {
        this.product_total_price = product_total_price;
    }
}
