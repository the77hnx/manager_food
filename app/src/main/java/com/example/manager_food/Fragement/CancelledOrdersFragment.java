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

import com.example.manager_food.Adapter.CancelledOrdersAdapter;
import com.example.manager_food.R;
import com.example.manager_food.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class CancelledOrdersFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private CancelledOrdersAdapter CancelledorderAdapter;
    private List<OrderItem> orderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancelled_orders, container, false);

        recyclerViewOrders = view.findViewById(R.id.recyclerViewCancelled);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            orderList = getArguments().getParcelableArrayList("orders");
            orderList = filterCancelledOrders(orderList);
        } else {
            orderList = new ArrayList<>();
        }

        List<OrderItem> filteredOrders = filterCancelledOrders(orderList);

        CancelledorderAdapter = new CancelledOrdersAdapter(getContext(), filteredOrders);
        recyclerViewOrders.setAdapter(CancelledorderAdapter);

        return view;
    }

    private List<OrderItem> filterCancelledOrders(List<OrderItem> orders) {
        List<OrderItem> newOrders = new ArrayList<>();
        if (orders != null) {
            for (OrderItem order : orders) {
                if ("ملغية".equals(order.getOrderStatus())) {
                    newOrders.add(order);
                }
            }
        }
        return newOrders;
    }

    public static CancelledOrdersFragment newInstance(List<OrderItem> orders) {
        CancelledOrdersFragment fragment = new CancelledOrdersFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("orders", new ArrayList<>(orders));
        fragment.setArguments(args);
        return fragment;
    }
}
