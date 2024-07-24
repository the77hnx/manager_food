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

public class OnloadingOrdersAdapter extends RecyclerView.Adapter<OnloadingOrdersAdapter.OnloadingOrderViewHolder> {

    private final Context context;
    private final List<OrderItem> orderList;

    public OnloadingOrdersAdapter(Context context, List<OrderItem> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OnloadingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frag_onloading_orders, parent, false);
        return new OnloadingOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnloadingOrderViewHolder holder, int position) {
        OrderItem order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OnloadingOrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView customerName;
        private final TextView orderDate;
        private final TextView orderId;
        private final TextView orderTotal;
        private final TextView itemName;
        private final TextView itemQuantity;
        private final TextView itemPrice;
        private final TextView orderMessage;

        public OnloadingOrderViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name_onloading);
            orderDate = itemView.findViewById(R.id.order_date_onloading);
            orderId = itemView.findViewById(R.id.order_id_onloading);
            orderTotal = itemView.findViewById(R.id.order_total_onloading);
            itemName = itemView.findViewById(R.id.item_name_onloading);
            itemQuantity = itemView.findViewById(R.id.item_quantity_onloading);
            itemPrice = itemView.findViewById(R.id.item_price_onloading);
            orderMessage = itemView.findViewById(R.id.order_message_onloading);
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
