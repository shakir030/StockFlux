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

public class sales_expense_adapter extends RecyclerView.Adapter<sales_expense_adapter.viewholder> implements Filterable {

    Context context;
    ArrayList<model_class_sales_expenses> sales_expense_list;
    ArrayList<model_class_sales_expenses> sales_expense_list_full;

    public sales_expense_adapter(Context context, ArrayList<model_class_sales_expenses> sales_expense_list) {
        this.context = context;
        this.sales_expense_list_full = sales_expense_list;
        this.sales_expense_list = new ArrayList<>(sales_expense_list_full);
    }

    @NonNull
    @Override
    public sales_expense_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sales_expense_item_recycle,parent,false);
        return new sales_expense_adapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull sales_expense_adapter.viewholder holder, int position) {
        model_class_sales_expenses model = sales_expense_list.get(position);
        holder.s_expense_name.setText(model.Name);
        holder.s_expense_price.setText(String.valueOf(model.Total_Price));

        Date date = model.Date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date_string = formatter.format(date);

        holder.s_expense_date.setText(date_string);


    }

    @Override
    public int getItemCount() {
        return sales_expense_list.size();
    }

    @Override
    public Filter getFilter() {
        return sales_expense_filter;
    }
    private final Filter sales_expense_filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<model_class_sales_expenses> filtered_sales_expense_list = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filtered_sales_expense_list.addAll(sales_expense_list_full);
            }
            else
            {
                String filterPatter = charSequence.toString().toLowerCase().trim();
                for(model_class_sales_expenses model_class_sales_expenses : sales_expense_list_full){

                    Date date = model_class_sales_expenses.Date;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String date_string = formatter.format(date);

                    if(model_class_sales_expenses.Name.contains(filterPatter)){
                        filtered_sales_expense_list.add(model_class_sales_expenses);
                    }
                    if(date_string.contains(filterPatter)){
                        filtered_sales_expense_list.add(model_class_sales_expenses);
                    }
                    String price = String.valueOf(model_class_sales_expenses.Total_Price);
                    if(price.contains(filterPatter)){
                        filtered_sales_expense_list.add(model_class_sales_expenses);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered_sales_expense_list;
            filterResults.count = filtered_sales_expense_list.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            sales_expense_list.clear();
            sales_expense_list.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();

        }
    };


    public static class viewholder extends RecyclerView.ViewHolder{
        TextView s_expense_name,s_expense_price,s_expense_date;
        CardView cardView_sales_expense;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            s_expense_name = itemView.findViewById(R.id.sales_expense_name_data);
            s_expense_price = itemView.findViewById(R.id.sales_expense_price_data);
            s_expense_date = itemView.findViewById(R.id.sales_expense_date_data);
            cardView_sales_expense = itemView.findViewById(R.id.sales_expense_card);
            cardView_sales_expense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(),sales_expense_item_details.class);
                    i.putExtra("expense_name",s_expense_name.getText().toString().trim());
                    i.putExtra("expense_date",s_expense_date.getText().toString().trim());
                    view.getContext().startActivity(i);
                }
            });

        }
    }
}
