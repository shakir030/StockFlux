package com.example.stockflux;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class sales_view_adapter extends RecyclerView.Adapter<sales_view_adapter.sales_viewholder> implements Filterable {
    Context context;
    ArrayList<sales_view_model> sales_array_list;
    ArrayList<sales_view_model> sales_array_list_full;


    public sales_view_adapter(Context context, ArrayList<sales_view_model> product_array_list) {
        this.context = context;
        this.sales_array_list_full = product_array_list;
        this.sales_array_list = new ArrayList<>(sales_array_list_full);
    }

    @NonNull
    @Override

    public sales_view_adapter.sales_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_sales_view,parent,false);
        return new sales_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull sales_view_adapter.sales_viewholder holder, int position) {
        sales_view_model model_data = sales_array_list.get(position);

        Date date = model_data.date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date_string = formatter.format(date);

        holder.product_sales_name.setText(model_data.name);
        holder.product_sales_qty.setText(String.valueOf(model_data.quantity));

        int total_price = model_data.quantity*model_data.per_price;

        holder.product_sales_total_price.setText(String.valueOf(total_price));
        holder.product_sales_date.setText(date_string);


    }

    @Override
    public int getItemCount() {
        return sales_array_list.size();
    }

    @Override
    public Filter getFilter() {
        return sales_view_filter;
    }
    private final Filter sales_view_filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<sales_view_model> filtered_sales_list = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filtered_sales_list.addAll(sales_array_list_full);
            }
            else
            {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (sales_view_model sales_view_model : sales_array_list_full){

                    Date date = sales_view_model.date;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String date_string = formatter.format(date);

                    if(sales_view_model.name.toLowerCase().contains(filterPattern)){
                        filtered_sales_list.add(sales_view_model);
                    }
                    if(date_string.contains(filterPattern)){
                        filtered_sales_list.add(sales_view_model);
                    }
                    String qty = String.valueOf(sales_view_model.quantity);
                    if(qty.contains(filterPattern)){
                        filtered_sales_list.add(sales_view_model);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered_sales_list;
            filterResults.count = filtered_sales_list.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            sales_array_list.clear();
            sales_array_list.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class sales_viewholder extends RecyclerView.ViewHolder{
        TextView product_sales_name,product_sales_qty,product_sales_total_price,product_sales_date;
        CardView card;

        public sales_viewholder(@NonNull View itemView) {
            super(itemView);

            product_sales_name = itemView.findViewById(R.id.sales_name_data);
            product_sales_qty = itemView.findViewById(R.id.sales_qty_data);
            product_sales_total_price = itemView.findViewById(R.id.sales_total_price_data);
            product_sales_date = itemView.findViewById(R.id.sales_date_data);
            card = itemView.findViewById(R.id.sales_view_card);


            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(),sales_product_view_details.class);
                    i.putExtra("product_sales_name",product_sales_name.getText().toString().trim());
                    i.putExtra("product_sales_date",product_sales_date.getText().toString().trim());
                    view.getContext().startActivity(i);
                }
            });

        }
    }
}
