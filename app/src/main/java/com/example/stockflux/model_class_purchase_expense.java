package com.example.stockflux;

public class model_class_purchase_expense {
    String purchase_expense_name,purchase_expense_description,purchase_expense_date;
    int purchase_expense_total_price;

    model_class_purchase_expense(){

    }

    public String getPurchase_expense_date() {
        return purchase_expense_date;
    }

    public void setPurchase_expense_date(String purchase_expense_date) {
        this.purchase_expense_date = purchase_expense_date;
    }

    public String getPurchase_expense_name() {
        return purchase_expense_name;
    }

    public void setPurchase_expense_name(String purchase_expense_name) {
        this.purchase_expense_name = purchase_expense_name;
    }

    public String getPurchase_expense_description() {
        return purchase_expense_description;
    }

    public void setPurchase_expense_description(String purchase_expense_description) {
        this.purchase_expense_description = purchase_expense_description;
    }

    public int getPurchase_expense_total_price() {
        return purchase_expense_total_price;
    }

    public void setPurchase_expense_total_price(int purchase_expense_total_price) {
        this.purchase_expense_total_price = purchase_expense_total_price;
    }
}
