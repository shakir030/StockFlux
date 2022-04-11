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

public class purchase_expense_adapter extends RecyclerView.Adapter<purchase_expense_adapter.viewholder> implements Filterable {

    Context context;
    ArrayList<purchase_expense_model> purchase_expense_list_full;
    ArrayList<purchase_expense_model> purchase_expense_list;

    public purchase_expense_adapter(Context context, ArrayList<purchase_expense_model> purchase_expense_list) {
        this.context = context;
        this.purchase_expense_list_full = purchase_expense_list;
        this.purchase_expense_list = new ArrayList<>(purchase_expense_list_full);
    }

    @NonNull
    @Override
    public purchase_expense_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.purchase_expense_item_recycle,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull purchase_expense_adapter.viewholder holder, int position) {
        purchase_expense_model model = purchase_expense_list.get(position);

        Date date = model.purchase_expense_date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date_string = formatter.format(date);


        holder.p_expense_name.setText(model.purchase_expense_name);
        holder.p_expense_price.setText(String.valueOf(model.purchase_expense_total_price));
        holder.p_expense_date.setText(date_string);


    }

    @Override
    public int getItemCount() {
        return purchase_expense_list.size();
    }

    @Override
    public Filter getFilter() {
        return purchase_expense_filter;
    }

    private final Filter purchase_expense_filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<purchase_expense_model> filtered_purchase_expense_list = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filtered_purchase_expense_list.addAll(purchase_expense_list_full);
            }
            else
            {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(purchase_expense_model purchase_expense_model : purchase_expense_list_full){

                    Date date = purchase_expense_model.getPurchase_expense_date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String date_string = formatter.format(date);

                    if(purchase_expense_model.purchase_expense_name.toLowerCase().contains(filterPattern)){
                        filtered_purchase_expense_list.add(purchase_expense_model);
                    }
                    if(date_string.contains(filterPattern)){
                        filtered_purchase_expense_list.add(purchase_expense_model);
                    }
                    String price = String.valueOf(purchase_expense_model.purchase_expense_total_price);
                    if(price.contains(filterPattern)){
                        filtered_purchase_expense_list.add(purchase_expense_model);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered_purchase_expense_list;
            results.count = filtered_purchase_expense_list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            purchase_expense_list.clear();
            purchase_expense_list.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();

        }
    };

    public static class viewholder extends RecyclerView.ViewHolder{
        TextView p_expense_name,p_expense_price,p_expense_date;
        CardView cardView_purchase_expense;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            p_expense_name = itemView.findViewById(R.id.purchase_expense_name_data);
            p_expense_price = itemView.findViewById(R.id.purchase_expense_price_data);
            p_expense_date = itemView.findViewById(R.id.purchase_expense_date_data);
            cardView_purchase_expense = itemView.findViewById(R.id.purchase_expense_card);
            cardView_purchase_expense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(),purchase_expense_item_details.class);
                    i.putExtra("expense_name",p_expense_name.getText().toString().trim());
                    i.putExtra("expense_date",p_expense_date.getText().toString().trim());
                    view.getContext().startActivity(i);
                }
            });

        }
    }
}
