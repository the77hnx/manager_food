package com.example.manager_food.Adapter;

// Similar imports as above
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
public class CompletedOrdersAdapter extends RecyclerView.Adapter<CompletedOrdersAdapter.CompletedOrderViewHolder> {

    private final Context context;
    private final List<OrderItem> orderList;
    private final OnOrderClickListener onOrderClickListener;

    private static final String COMPLETED_STATUS = "Completed";
    private static final int COMPLETED_STATUS_ID = 6; // Assuming 6 corresponds to "Completed"

    public CompletedOrdersAdapter(Context context, List<OrderItem> orderList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.onOrderClickListener = onOrderClickListener;
        this.orderList = new ArrayList<>();
        setOrderList(orderList); // Initialize the order list

    }

    @NonNull
    @Override
    public CompletedOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frag_cancelled_order, parent, false);
        return new CompletedOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrderViewHolder holder, int position) {
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
            if (COMPLETED_STATUS.equals(order.getOrderStatus()) || order.getIdStatutCommande() == COMPLETED_STATUS_ID) {
                orderList.add(order); // Add only new orders
            }
        }
        notifyDataSetChanged(); // Notify the adapter of data change
    }
    public interface OnOrderClickListener {
        void onOrderClick(OrderItem order);
    }

    static class CompletedOrderViewHolder extends RecyclerView.ViewHolder {
        // Similar fields and constructor as NewOrderViewHolder
        private final TextView customerName;
        private final TextView orderDate;
        private final TextView orderId;
        private final TextView orderTotal;
        private final TextView orderMessage;
        private final TextView orderStatus;
        private final RecyclerView itemsRecyclerView;
        public CompletedOrderViewHolder(View itemView) {
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
