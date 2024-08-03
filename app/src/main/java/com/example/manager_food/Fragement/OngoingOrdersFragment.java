package com.example.manager_food.Fragement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager_food.Adapter.OngoingOrdersAdapter;
import com.example.manager_food.OngoingOrderActivity;
import com.example.manager_food.R;
import com.example.manager_food.model.OrderItem;

import java.util.ArrayList;
import java.util.List;
public class OngoingOrdersFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private OngoingOrdersAdapter orderAdapter;
    private List<OrderItem> orderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ongoing_orders, container, false);

        recyclerViewOrders = view.findViewById(R.id.recyclerViewOngoingOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            orderList = getArguments().getParcelableArrayList("orders");
            orderList = filterOngoingOrders(orderList);
        } else {
            orderList = new ArrayList<>();
        }

        List<OrderItem> filteredOrders = filterOngoingOrders(orderList);

        orderAdapter = new OngoingOrdersAdapter(getContext(), filteredOrders, this::onOrderClick);
        recyclerViewOrders.setAdapter(orderAdapter);

        return view;
    }

    private List<OrderItem> filterOngoingOrders(List<OrderItem> orders) {
        List<OrderItem> ongoingOrders = new ArrayList<>();
        if (orders != null) {
            for (OrderItem order : orders) {
                if ("قيد التحضير".equals(order.getOrderStatus())) {
                    ongoingOrders.add(order);
                }
            }
        }
        return ongoingOrders;
    }

    public static OngoingOrdersFragment newInstance(List<OrderItem> orders) {
        OngoingOrdersFragment fragment = new OngoingOrdersFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("orders", new ArrayList<>(orders));
        fragment.setArguments(args);
        return fragment;
    }

    private void onOrderClick(OrderItem order) {
        Intent intent = new Intent(getActivity(), OngoingOrderActivity.class);
        intent.putExtra("CUSTOMER_NAME", order.getCustomerName());
        intent.putExtra("ORDER_DATE", order.getOrderDate());
        intent.putExtra("ORDER_TOTAL", order.getOrderTotal());
        intent.putExtra("ITEM_NAME", order.getItemName());
        intent.putExtra("ITEM_QUANTITY", order.getItemQuantity());
        intent.putExtra("ITEM_PRICE", order.getItemPrice());
        intent.putExtra("ORDER_MESSAGE", order.getOrderMessage());
        intent.putExtra("ORDER_STATUS", order.getOrderStatus());
        startActivity(intent);
    }
}
