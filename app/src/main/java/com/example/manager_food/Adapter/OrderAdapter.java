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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderItem> orderList;
    private final LayoutInflater inflater;

    public OrderAdapter(Context context, List<OrderItem> orderList) {
        this.orderList = orderList;
        this.inflater = LayoutInflater.from(context);
    }

    public void updateData(List<OrderItem> newOrderList) {
        this.orderList = newOrderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.frag_cancelled_order, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem orderItem = orderList.get(position);
        holder.customerName.setText(orderItem.getCustomerName());
        holder.orderDate.setText(orderItem.getOrderDate());
        holder.orderId.setText("رقم الطلب: " + orderItem.getOrderId());
        holder.orderTotal.setText("الإجمالي: " + orderItem.getOrderTotal());
        holder.orderStatus.setText("حالة الطلب : " + orderItem.getOrderStatus());
        holder.itemName.setText(orderItem.getItemName());
        holder.itemQuantity.setText("الكمية: " + orderItem.getItemQuantity());
        holder.itemPrice.setText(orderItem.getItemPrice());
        holder.orderMessage.setText("الرسالة: " + orderItem.getOrderMessage());
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, orderDate, orderId, orderTotal, orderStatus, itemName, itemQuantity, itemPrice, orderMessage;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name_cancelled);
            orderDate = itemView.findViewById(R.id.order_date_cancelled);
            orderId = itemView.findViewById(R.id.order_id_cancelled);
            orderTotal = itemView.findViewById(R.id.order_total_cancelled);
            itemName = itemView.findViewById(R.id.item_name_cancelled);
            itemQuantity = itemView.findViewById(R.id.item_quantity_cancelled);
            itemPrice = itemView.findViewById(R.id.item_price_cancelled);
            orderMessage = itemView.findViewById(R.id.order_message_cancelled);
            orderStatus = itemView.findViewById(R.id.order_Status_tv_cancelled);
        }
    }
}
