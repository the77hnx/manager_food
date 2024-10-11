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

public class CancelledOrdersAdapter extends RecyclerView.Adapter<CancelledOrdersAdapter.CancelledOrderViewHolder> {

    private final Context context;
    private final List<OrderItem> orderList;
    private final OnOrderClickListener onOrderClickListener;

    // Define a constant for the "Cancelled" status
    private static final String CANCELLED_STATUS = "Cancelled";
    private static final int CANCELLED_STATUS_ID = 5; // Assuming 3 corresponds to "Cancelled"

    public CancelledOrdersAdapter(Context context, List<OrderItem> orderList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.onOrderClickListener = onOrderClickListener;

        // Filter the orders to show only cancelled ones
        this.orderList = new ArrayList<>();
        setOrderList(orderList); // Initialize the order list

    }

    @NonNull
    @Override
    public CancelledOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frag_cancelled_order, parent, false);
        return new CancelledOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CancelledOrderViewHolder holder, int position) {
        OrderItem order = orderList.get(position);
        holder.bind(order);
        holder.itemView.setOnClickListener(v -> onOrderClickListener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    public void setOrderList(List<OrderItem> newOrderList) {
        orderList.clear(); // Clear existing items
        for (OrderItem order : newOrderList) {
            // Filter based on status
            if (CANCELLED_STATUS.equals(order.getOrderStatus()) || order.getIdStatutCommande() == CANCELLED_STATUS_ID) {
                orderList.add(order); // Add only new orders
            }
        }
        notifyDataSetChanged(); // Notify the adapter of data change
    }
    public interface OnOrderClickListener {
        void onOrderClick(OrderItem order);
    }

    static class CancelledOrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView customerName;
        private final TextView orderDate;
        private final TextView orderId;
        private final TextView orderTotal;
        private final TextView orderMessage;
        private  final TextView orderStatus;
        private final RecyclerView itemsRecyclerView; // New RecyclerView for order items

        public CancelledOrderViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name_cancelled_order);
            orderDate = itemView.findViewById(R.id.order_date_cancelled_order);
            orderId = itemView.findViewById(R.id.order_id_cancelled_order);
            orderStatus = itemView.findViewById(R.id.order_Status_tv_cancelled_oder);
            orderTotal = itemView.findViewById(R.id.order_total_cancelled_order);
            orderMessage = itemView.findViewById(R.id.order_message_cancelled_order);
            itemsRecyclerView = itemView.findViewById(R.id.recycler_view_cancelled_items); // Initialize RecyclerView for items
        }

        public void bind(OrderItem order) {
            customerName.setText("اسم الزبون : " + order.getCustomerName());
            orderDate.setText( "تاريخ الطلب : " + order.getOrderDate());
            orderId.setText("ايدي الطلب : " + order.getOrderId());
            orderTotal.setText(String.format("السعر الاجمالي : %s", order.getOrderTotal(), itemView.getContext()) + "دج"); // Assuming you have a currency symbol in resources
            orderMessage.setText(order.getOrderMessage());
            orderStatus.setText("حالة الطلب : " + order.getOrderStatus());
            OrderItemsAdapter itemsAdapter = new OrderItemsAdapter(order.getItems());
            itemsRecyclerView.setAdapter(itemsAdapter);
            itemsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}
