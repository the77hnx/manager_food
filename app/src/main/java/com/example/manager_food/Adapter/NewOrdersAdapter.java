package com.example.manager_food.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager_food.R;
import com.example.manager_food.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class NewOrdersAdapter extends RecyclerView.Adapter<NewOrdersAdapter.NewOrderViewHolder> {

    private final Context context;
    private final List<OrderItem> orderList;
    private final OnOrderClickListener onOrderClickListener;

    private static final String NEW_STATUS = "New";
    private static final int NEW_STATUS_ID = 1; // Assuming 1 corresponds to "New"

    public NewOrdersAdapter(Context context, List<OrderItem> orderList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.onOrderClickListener = onOrderClickListener;
        this.orderList = new ArrayList<>();
        for (OrderItem order : orderList) {
            if (NEW_STATUS.equals(order.getOrderStatus()) || order.getIdStatutCommande() == NEW_STATUS_ID) {
                this.orderList.add(order);
            }
        }
    }

    @NonNull
    @Override
    public NewOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frag_cancelled_order, parent, false);
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

    public interface OnOrderClickListener {
        void onOrderClick(OrderItem order);
    }

    static class NewOrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView customerName;
        private final TextView orderDate;
        private final TextView orderId;
        private final TextView orderTotal;
        private final TextView orderMessage;
        private final TextView orderStatus;
        private final RecyclerView itemsRecyclerView;

        public NewOrderViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name_cancelled_order);
            orderDate = itemView.findViewById(R.id.order_date_cancelled_order);
            orderId = itemView.findViewById(R.id.order_id_cancelled_order);
            orderStatus = itemView.findViewById(R.id.order_Status_tv_cancelled_oder);
            orderTotal = itemView.findViewById(R.id.order_total_cancelled_order);
            orderMessage = itemView.findViewById(R.id.order_message_cancelled_order);
            itemsRecyclerView = itemView.findViewById(R.id.recycler_view_cancelled_items);
        }

        public void bind(OrderItem order) {
            customerName.setText(order.getCustomerName());
            orderDate.setText(order.getOrderDate());
            orderId.setText(order.getOrderId());
            orderTotal.setText(String.format("%s %s", order.getOrderTotal(), itemView.getContext()));
            orderMessage.setText(order.getOrderMessage());
            orderStatus.setText(order.getOrderStatus());
            OrderItemsAdapter itemsAdapter = new OrderItemsAdapter(order.getItems());
            itemsRecyclerView.setAdapter(itemsAdapter);
            itemsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}
