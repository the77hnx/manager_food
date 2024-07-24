package com.example.manager_food.Fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager_food.Adapter.NewOrdersAdapter;
import com.example.manager_food.R;
import com.example.manager_food.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class NewOrdersFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private NewOrdersAdapter orderAdapter;
    private List<OrderItem> orderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_orders, container, false);

        recyclerViewOrders = view.findViewById(R.id.recyclerViewNewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            orderList = getArguments().getParcelableArrayList("orders");
            orderList = filterNewOrders(orderList);
        } else {
            orderList = new ArrayList<>();
        }

        List<OrderItem> filteredOrders = filterNewOrders(orderList);

        orderAdapter = new NewOrdersAdapter(getContext(), filteredOrders);
        recyclerViewOrders.setAdapter(orderAdapter);

        return view;
    }

    private List<OrderItem> filterNewOrders(List<OrderItem> orders) {
        List<OrderItem> newOrders = new ArrayList<>();
        if (orders != null) {
            for (OrderItem order : orders) {
                if ("جديد".equals(order.getOrderStatus())) {
                    newOrders.add(order);
                }
            }
        }
        return newOrders;
    }

    public static NewOrdersFragment newInstance(List<OrderItem> orders) {
        NewOrdersFragment fragment = new NewOrdersFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("orders", new ArrayList<>(orders));
        fragment.setArguments(args);
        return fragment;
    }
}
