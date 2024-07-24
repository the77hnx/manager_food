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

public class OrdersPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_NEW = 0;
    private static final int VIEW_TYPE_IN_PREPARATION = 1;
    private static final int VIEW_TYPE_IN_DELIVERY = 2;
    private static final int VIEW_TYPE_CANCELLED = 3;

    private final Context context;
    private final List<OrderItem> newOrders;
    private final List<OrderItem> ongoingOrders;
    private final List<OrderItem> onloadingOrders;
    private final List<OrderItem> cancelledOrders;

    public OrdersPagerAdapter(Context context,
                              List<OrderItem> newOrders,
                              List<OrderItem> ongoingOrders,
                              List<OrderItem> onloadingOrders,
                              List<OrderItem> cancelledOrders) {
        this.context = context;
        this.newOrders = newOrders;
        this.ongoingOrders = ongoingOrders;
        this.onloadingOrders = onloadingOrders;
        this.cancelledOrders = cancelledOrders;
    }

    @Override
    public int getItemViewType(int position) {
        // Return the view type based on the position
        if (position < newOrders.size()) {
            return VIEW_TYPE_NEW;
        } else if (position < newOrders.size() + ongoingOrders.size()) {
            return VIEW_TYPE_IN_PREPARATION;
        } else if (position < newOrders.size() + ongoingOrders.size() + onloadingOrders.size()) {
            return VIEW_TYPE_IN_DELIVERY;
        } else {
            return VIEW_TYPE_CANCELLED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case VIEW_TYPE_NEW:
                return new NewOrderViewHolder(inflater.inflate(R.layout.frag_new_order, parent, false));
            case VIEW_TYPE_IN_PREPARATION:
                return new OngoingOrderViewHolder(inflater.inflate(R.layout.frag_ongoing_order, parent, false));
            case VIEW_TYPE_IN_DELIVERY:
                return new OnloadingOrderViewHolder(inflater.inflate(R.layout.frag_onloading_orders, parent, false));
            default:
                return new CancelledOrderViewHolder(inflater.inflate(R.layout.frag_cancelled_order, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < newOrders.size()) {
            ((NewOrderViewHolder) holder).bind(newOrders.get(position));
        } else if (position < newOrders.size() + ongoingOrders.size()) {
            ((OngoingOrderViewHolder) holder).bind(ongoingOrders.get(position - newOrders.size()));
        } else if (position < newOrders.size() + ongoingOrders.size() + onloadingOrders.size()) {
            ((OnloadingOrderViewHolder) holder).bind(onloadingOrders.get(position - newOrders.size() - ongoingOrders.size()));
        } else {
            ((CancelledOrderViewHolder) holder).bind(cancelledOrders.get(position - newOrders.size() - ongoingOrders.size() - onloadingOrders.size()));
        }
    }

    @Override
    public int getItemCount() {
        return newOrders.size() + ongoingOrders.size() + onloadingOrders.size() + cancelledOrders.size();
    }

    // View Holder for New Orders
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
            customerName = itemView.findViewById(R.id.customer_name_new);
            orderDate = itemView.findViewById(R.id.order_date_new);
            orderId = itemView.findViewById(R.id.order_id_new);
            orderTotal = itemView.findViewById(R.id.order_total_new);
            itemName = itemView.findViewById(R.id.item_name_new);
            itemQuantity = itemView.findViewById(R.id.item_quantity_new);
            itemPrice = itemView.findViewById(R.id.item_price_new);
            orderMessage = itemView.findViewById(R.id.order_message_new);
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

    // View Holder for Orders in Preparation
    static class OngoingOrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView customerName;
        private final TextView orderDate;
        private final TextView orderId;
        private final TextView orderTotal;
        private final TextView itemName;
        private final TextView itemQuantity;
        private final TextView itemPrice;
        private final TextView orderMessage;

        public OngoingOrderViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name_ongoing);
            orderDate = itemView.findViewById(R.id.order_date_ongoing);
            orderId = itemView.findViewById(R.id.order_id_ongoing);
            orderTotal = itemView.findViewById(R.id.order_total_ongoing);
            itemName = itemView.findViewById(R.id.item_name_ongoing);
            itemQuantity = itemView.findViewById(R.id.item_quantity_ongoing);
            itemPrice = itemView.findViewById(R.id.item_price_ongoing);
            orderMessage = itemView.findViewById(R.id.order_message_ongoing);
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

    // View Holder for Orders in Delivery
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

    // View Holder for Cancelled Orders
    static class CancelledOrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView customerName;
        private final TextView orderDate;
        private final TextView orderId;
        private final TextView orderTotal;
        private final TextView itemName;
        private final TextView itemQuantity;
        private final TextView itemPrice;
        private final TextView orderMessage;

        public CancelledOrderViewHolder(View itemView) {
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
