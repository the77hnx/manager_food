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

import com.example.manager_food.Adapter.NewOrdersAdapter;
import com.example.manager_food.DetailsOrderActivity;
import com.example.manager_food.NewOrderActivity;
import com.example.manager_food.R;
import com.example.manager_food.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class NewOrdersFragment extends Fragment implements NewOrdersAdapter.OnOrderClickListener {

    private RecyclerView recyclerViewOrders;
    private NewOrdersAdapter newOrderAdapter;
    private List<OrderItem> orderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_orders, container, false);

        initializeViews(view);
        setupRecyclerView();

        if (getArguments() != null) {
            orderList = getArguments().getParcelableArrayList("orders");
            orderList = filterNewOrders(orderList);
        } else {
            orderList = new ArrayList<>();
        }

        newOrderAdapter = new NewOrdersAdapter(getContext(), orderList, this::onOrderClick);
        recyclerViewOrders.setAdapter(newOrderAdapter);

        return view;
    }

    private void initializeViews(View view) {
        recyclerViewOrders = view.findViewById(R.id.recyclerViewNewOrders);
    }

    private void setupRecyclerView() {
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private List<OrderItem> filterNewOrders(List<OrderItem> orders) {
        List<OrderItem> newOrders = new ArrayList<>();
        if (orders != null) {
            for (OrderItem order : orders) {
                if ("جديدة".equals(order.getOrderStatus())) { // Arabic for "New"
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

    @Override
    public void onOrderClick(OrderItem order) {
        Intent intent = new Intent(getActivity(), DetailsOrderActivity.class);
        intent.putExtra("CUSTOMER_NAME", order.getCustomerName());
        intent.putExtra("ORDER_DATE", order.getOrderDate());
        intent.putExtra("ORDER_TOTAL", order.getOrderTotal());
        intent.putExtra("ORDER_MESSAGE", order.getOrderMessage());
        intent.putExtra("ORDER_STATUS", order.getOrderStatus());
        startActivity(intent);
    }
}
