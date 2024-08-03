package com.example.manager_food.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager_food.R;
import com.example.manager_food.model.OrderItem;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderItem> orders;
    private OnOrderClickListener onOrderClickListener;

    public interface OnOrderClickListener {
        void onOrderClick(OrderItem order);
    }

    public OrderAdapter(List<OrderItem> orders, OnOrderClickListener onOrderClickListener) {
        this.orders = orders;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_cancelled_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem order = orders.get(position);
        holder.bind(order, onOrderClickListener);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderSummary;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderSummary = itemView.findViewById(R.id.order_total_cancelled); // Update with actual ID
        }

        public void bind(final OrderItem order, final OnOrderClickListener listener) {
            // Bind order data to view
            orderSummary.setText(order.getOrderTotal()); // Adjust based on your layout

            itemView.setOnClickListener(v -> listener.onOrderClick(order));
        }
    }
}
