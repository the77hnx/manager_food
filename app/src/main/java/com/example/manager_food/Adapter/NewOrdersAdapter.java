package com.example.manager_food.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager_food.R;
import com.example.manager_food.model.OrderItem;

import java.util.List;

public class NewOrdersAdapter extends RecyclerView.Adapter<NewOrdersAdapter.NewOrderViewHolder> {

    private final Context context;
    private final List<OrderItem> orderList;
    private final OnOrderClickListener onOrderClickListener;

    public interface OnOrderClickListener {
        void onOrderClick(OrderItem order);
    }

    public NewOrdersAdapter(Context context, List<OrderItem> orderList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public NewOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frag_new_order, parent, false);
        return new NewOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewOrderViewHolder holder, int position) {
        OrderItem order = orderList.get(position);
        holder.bind(order);
        holder.itemView.setOnClickListener(v -> onOrderClickListener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class NewOrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView customerName;
        private final TextView orderDate;
        private final TextView orderId;
        private final TextView orderTotal;
        private final TextView itemName;
        private final TextView itemQuantity;
        private final TextView itemPrice;
        private final TextView orderMessage;

        public NewOrderViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name_cancelled);
            orderDate = itemView.findViewById(R.id.order_date_cancelled);
            orderId = itemView.findViewById(R.id.order_id_cancelled);
            orderTotal = itemView.findViewById(R.id.order_total_cancelled);
            itemName = itemView.findViewById(R.id.item_name_cancelled);
            itemQuantity = itemView.findViewById(R.id.item_quantity_cancelled);
            itemPrice = itemView.findViewById(R.id.item_price_cancelled);
            orderMessage = itemView.findViewById(R.id.order_message_cancelled);
        }

        public void bind(OrderItem order) {
            customerName.setText(order.getCustomerName());
            orderDate.setText(order.getOrderDate());
            orderId.setText(order.getOrderId());
            orderTotal.setText(order.getOrderTotal());
            itemName.setText(order.getItemName());
            itemQuantity.setText(order.getItemQuantity());
            itemPrice.setText(order.getItemPrice());
            orderMessage.setText(order.getOrderMessage());
        }
    }
}
