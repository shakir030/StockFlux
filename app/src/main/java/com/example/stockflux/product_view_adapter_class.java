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

public class product_view_adapter_class extends RecyclerView.Adapter<product_view_adapter_class.product_viewholder> implements Filterable {
    Context context;
    ArrayList<product_view_model_class> product_array_list;
    ArrayList<product_view_model_class> product_array_list_full;

    public product_view_adapter_class(Context context, ArrayList<product_view_model_class> product_array_list) {
        this.context = context;
        this.product_array_list_full = product_array_list;
        this.product_array_list = new ArrayList<>(product_array_list_full);
    }

    @NonNull
    @Override
    public product_view_adapter_class.product_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_recycle_view,parent,false);
        return new product_viewholder(v);
    }

    Date date_formated;
    String date_string;
    @Override
    public void onBindViewHolder(@NonNull product_view_adapter_class.product_viewholder holder, int position) {
        product_view_model_class get_data = product_array_list.get(position);

            date_formated = get_data.getProduct_date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            date_string = formatter.format(date_formated);


        holder.product_name.setText(get_data.getProduct_name());
        holder.product_qty.setText(String.valueOf(get_data.getProduct_qty()));
        holder.product_total_price.setText(String.valueOf(get_data.getProduct_total_price()));
        holder.product_date.setText(date_string);

    }

    @Override
    public int getItemCount() {
        return product_array_list.size();
    }

    @Override
    public Filter getFilter() {
        return purchase_view_filter;
    }

    private final Filter purchase_view_filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<product_view_model_class> filtered_purchase_list = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filtered_purchase_list.addAll(product_array_list_full);
            }
            else
            {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(product_view_model_class product_view_model_class : product_array_list_full){
                    if(product_view_model_class.product_name.toLowerCase().contains(filterPattern)){
                        filtered_purchase_list.add(product_view_model_class);
                    }
                    if(date_string.contains(filterPattern)){
                        filtered_purchase_list.add(product_view_model_class);
                    }
                    String qty = String.valueOf(product_view_model_class.product_qty);
                    if(qty.contains(filterPattern)){
                        filtered_purchase_list.add(product_view_model_class);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered_purchase_list;
            results.count = filtered_purchase_list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            product_array_list.clear();
            product_array_list.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();

        }
    };

    public static class product_viewholder extends RecyclerView.ViewHolder{
        TextView product_name,product_qty,product_total_price,product_date;
        CardView card;

        public product_viewholder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name_data);
            product_qty = itemView.findViewById(R.id.product_qty_data);
            product_total_price = itemView.findViewById(R.id.product_total_price_data);
            product_date = itemView.findViewById(R.id.product_date_data);
            card = itemView.findViewById(R.id.product_view_card);
            

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(),prodcut_card_view_details.class);
                    i.putExtra("product_name",product_name.getText().toString().trim());
                    i.putExtra("product_date",product_date.getText().toString().trim());
                    view.getContext().startActivity(i);
                }
            });


        }
    }
}
