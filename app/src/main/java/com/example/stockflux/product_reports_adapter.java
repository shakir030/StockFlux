package com.example.stockflux;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class product_reports_adapter extends RecyclerView.Adapter<product_reports_adapter.ViewHolder> implements Filterable {

    Context context;
    ArrayList<model_products_reports> products_list;
    ArrayList<model_products_reports> products_list_full;

    public product_reports_adapter(Context context,ArrayList<model_products_reports> products_list) {
        this.context = context;
        this.products_list_full = products_list;
        this.products_list = new ArrayList<>(products_list_full);
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

            Date date = model.getProduct_date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String date_string = formatter.format(date);

            holder.product_date.setText(date_string);
            holder.product_qty.setText(String.valueOf(model.product_qty));

    }

    @Override
    public int getItemCount() {
        return products_list.size();
    }

    @Override
    public Filter getFilter() {

        return product_reports_filter;
    }

    private final Filter product_reports_filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<model_products_reports> filtered_products_reports = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filtered_products_reports.addAll(products_list_full);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (model_products_reports model_products_reports : products_list_full) {

                    Date date = model_products_reports.getProduct_date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String date_string = formatter.format(date);

                    if (model_products_reports.product_name.toLowerCase().contains(filterPattern)) {
                        filtered_products_reports.add(model_products_reports);
                    }
                    if (date_string.contains(filterPattern)) {
                        filtered_products_reports.add(model_products_reports);
                    }
                    String qty = String.valueOf(model_products_reports.product_qty);
                    if (qty.contains(filterPattern)) {
                        filtered_products_reports.add(model_products_reports);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered_products_reports;
            results.count = filtered_products_reports.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            products_list.clear();
            products_list.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();

        }
    };

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