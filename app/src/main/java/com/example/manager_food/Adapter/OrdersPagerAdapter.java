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
    private static final int VIEW_TYPE_ON_DELIVERY = 3; // Added missing semicolon
    private static final int VIEW_TYPE_CANCELLED = 4;
    private static final int VIEW_TYPE_COMPLETED = 5;

    private final Context context;
    private final List<OrderItem> newOrders;
    private final List<OrderItem> ongoingOrders;
    private final List<OrderItem> inDeliveryOrders;  // Consider renaming this for clarity
    private final List<OrderItem> ondeliveryOrders;
    private final List<OrderItem> cancelledOrders;
    private final List<OrderItem> completedOrders;


    public interface OnOrderClickListener {
        void onOrderClick(OrderItem order);
    }


    public OrdersPagerAdapter(Context context,
                              List<OrderItem> newOrders,
                              List<OrderItem> ongoingOrders,
                              List<OrderItem> inDeliveryOrders,
                              List<OrderItem> ondeliveryOrders,
                              List<OrderItem> cancelledOrders,
                              List<OrderItem> completedOrders) {
        this.context = context;
        this.newOrders = newOrders;
        this.ongoingOrders = ongoingOrders;
        this.inDeliveryOrders = inDeliveryOrders;
        this.ondeliveryOrders = ondeliveryOrders;
        this.cancelledOrders = cancelledOrders;
        this.completedOrders = completedOrders;  // Added line
    }


    @Override
    public int getItemViewType(int position) {
        if (position < newOrders.size()) {
            return VIEW_TYPE_NEW;
        } else if (position < newOrders.size() + ongoingOrders.size()) {
            return VIEW_TYPE_IN_PREPARATION;
        } else if (position < newOrders.size() + ongoingOrders.size() + inDeliveryOrders.size()) {
            return VIEW_TYPE_IN_DELIVERY;
        } else if (position < newOrders.size() + ongoingOrders.size() + inDeliveryOrders.size() + ondeliveryOrders.size()) {
            return VIEW_TYPE_ON_DELIVERY;
        } else if (position < newOrders.size() + ongoingOrders.size() + inDeliveryOrders.size() + ondeliveryOrders.size() + cancelledOrders.size()) {
            return VIEW_TYPE_CANCELLED;
        } else {
            return VIEW_TYPE_COMPLETED;
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
            case VIEW_TYPE_ON_DELIVERY:
                return new OndeliveryOrderViewHolder(inflater.inflate(R.layout.frag_ondelivery_orders, parent, false));
            case VIEW_TYPE_CANCELLED:
                return new CancelledOrderViewHolder(inflater.inflate(R.layout.frag_cancelled_order, parent, false));
            default:
                return new CompletedOrderViewHolder(inflater.inflate(R.layout.frag_completed_order, parent, false));  // Added line
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < newOrders.size()) {
            ((NewOrderViewHolder) holder).bind(newOrders.get(position));
        } else if (position < newOrders.size() + ongoingOrders.size()) {
            ((OngoingOrderViewHolder) holder).bind(ongoingOrders.get(position - newOrders.size()));
        } else if (position < newOrders.size() + ongoingOrders.size() + inDeliveryOrders.size()) {
            ((OnloadingOrderViewHolder) holder).bind(inDeliveryOrders.get(position - newOrders.size() - ongoingOrders.size()));
        } else if (position < newOrders.size() + ongoingOrders.size() + inDeliveryOrders.size() + ondeliveryOrders.size()) {
            ((OndeliveryOrderViewHolder) holder).bind(ondeliveryOrders.get(position - newOrders.size() - ongoingOrders.size() - inDeliveryOrders.size()));
        } else if (position < newOrders.size() + ongoingOrders.size() + inDeliveryOrders.size() + ondeliveryOrders.size() + cancelledOrders.size()) {
            ((CancelledOrderViewHolder) holder).bind(cancelledOrders.get(position - newOrders.size() - ongoingOrders.size() - inDeliveryOrders.size() - ondeliveryOrders.size()));
        } else {
            ((CompletedOrderViewHolder) holder).bind(completedOrders.get(position - newOrders.size() - ongoingOrders.size() - inDeliveryOrders.size() - ondeliveryOrders.size() - cancelledOrders.size())); // Updated line
        }
    }


    @Override
    public int getItemCount() {
        return newOrders.size() +
                ongoingOrders.size() +
                inDeliveryOrders.size() +
                ondeliveryOrders.size() +
                cancelledOrders.size() +
                completedOrders.size();  // Added line
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
            customerName = itemView.findViewById(R.id.customer_name_cancelled);  // Ensure unique ID
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

    // View Holder for Ongoing Orders
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
            customerName = itemView.findViewById(R.id.customer_name_cancelled);  // Ensure unique ID
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

    static class CompletedOrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView customerName;
        private final TextView orderDate;
        private final TextView orderId;
        private final TextView orderTotal;
        private final TextView itemName;
        private final TextView itemQuantity;
        private final TextView itemPrice;
        private final TextView orderMessage;

        public CompletedOrderViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name_cancelled);  // Ensure unique ID
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


    // View Holder for In Delivery Orders
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
            customerName = itemView.findViewById(R.id.customer_name_cancelled);  // Ensure unique ID
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

    // View Holder for On Delivery Orders
    static class OndeliveryOrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView customerName;
        private final TextView orderDate;
        private final TextView orderId;
        private final TextView orderTotal;
        private final TextView itemName;
        private final TextView itemQuantity;
        private final TextView itemPrice;
        private final TextView orderMessage;

        public OndeliveryOrderViewHolder(View itemView) {  // Corrected constructor name
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name_cancelled);  // Ensure unique ID
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
            customerName = itemView.findViewById(R.id.customer_name_cancelled);  // Ensure unique ID
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
