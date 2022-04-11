package com.example.stockflux;

import java.util.Date;

public class purchase_expense_model {
    String purchase_expense_name;
    Date purchase_expense_date;
    int purchase_expense_total_price;

    public purchase_expense_model(){

    }
    public purchase_expense_model(String purchase_expense_name, Date purchase_expense_date, int purchase_expense_total_price) {
        this.purchase_expense_name = purchase_expense_name;
        this.purchase_expense_date = purchase_expense_date;
        this.purchase_expense_total_price = purchase_expense_total_price;
    }

    public String getPurchase_expense_name() {
        return purchase_expense_name;
    }

    public void setPurchase_expense_name(String purchase_expense_name) {
        this.purchase_expense_name = purchase_expense_name;
    }

    public Date getPurchase_expense_date() {
        return purchase_expense_date;
    }

    public void setPurchase_expense_date(Date purchase_expense_date) {
        this.purchase_expense_date = purchase_expense_date;
    }

    public int getPurchase_expense_total_price() {
        return purchase_expense_total_price;
    }

    public void setPurchase_expense_total_price(int purchase_expense_total_price) {
        this.purchase_expense_total_price = purchase_expense_total_price;
    }
}
