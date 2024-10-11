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
public class AwaitingDeliveryOrdersAdapter extends RecyclerView.Adapter<AwaitingDeliveryOrdersAdapter.AwaitingDeliveryOrderViewHolder> {

    private final Context context;
    private final List<OrderItem> orderList;
    private final OnOrderClickListener onOrderClickListener;

    private static final String AWAITING_DELIVERY_STATUS = "AwaitingDelivery";
    private static final int AWAITING_DELIVERY_STATUS_ID = 4; // Assuming 4 corresponds to "AwaitingDelivery"

    public AwaitingDeliveryOrdersAdapter(Context context, List<OrderItem> orderList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.onOrderClickListener = onOrderClickListener;
        this.orderList = new ArrayList<>();
        setOrderList(orderList); // Initialize the order list

    }

    @NonNull
    @Override
    public AwaitingDeliveryOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frag_cancelled_order, parent, false);
        return new AwaitingDeliveryOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AwaitingDeliveryOrderViewHolder holder, int position) {
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
            if (AWAITING_DELIVERY_STATUS.equals(order.getOrderStatus()) || order.getIdStatutCommande() == AWAITING_DELIVERY_STATUS_ID) {
                orderList.add(order); // Add only new orders
            }
        }
        notifyDataSetChanged(); // Notify the adapter of data change
    }
    public interface OnOrderClickListener {
        void onOrderClick(OrderItem order);
    }

    static class AwaitingDeliveryOrderViewHolder extends RecyclerView.ViewHolder {
        // Similar fields and constructor as NewOrderViewHolder
        private final TextView customerName;
        private final TextView orderDate;
        private final TextView orderId;
        private final TextView orderTotal;
        private final TextView orderMessage;
        private final TextView orderStatus;
        private final RecyclerView itemsRecyclerView;
        public AwaitingDeliveryOrderViewHolder(View itemView) {
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
