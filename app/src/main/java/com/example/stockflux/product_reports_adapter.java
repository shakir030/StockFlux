package com.example.stockflux;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class product_reports_adapter extends RecyclerView.Adapter<product_reports_adapter.ViewHolder> {

    Context context;
    ArrayList<model_products_reports> products_list;

    public product_reports_adapter(Context context,ArrayList<model_products_reports> products_list) {
        this.context = context;
        this.products_list = products_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_reports_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            model_products_reports model = products_list.get(position);
            holder.product_name.setText(model.product_name);
            holder.product_date.setText(model.product_date);
            holder.product_qty.setText(String.valueOf(model.product_qty));

    }

    @Override
    public int getItemCount() {
        return products_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_name,product_qty,product_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.Product_name);
            product_date = itemView.findViewById(R.id.product_date);
            product_qty = itemView.findViewById(R.id.product_qty);

        }
    }
}