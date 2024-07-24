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

import com.example.manager_food.Adapter.OngoingOrdersAdapter;
import com.example.manager_food.R;
import com.example.manager_food.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OngoingOrdersFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private OngoingOrdersAdapter ongoingOrdersAdapter;
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

        ongoingOrdersAdapter = new OngoingOrdersAdapter(getContext(), filteredOrders);
        recyclerViewOrders.setAdapter(ongoingOrdersAdapter);

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
}
