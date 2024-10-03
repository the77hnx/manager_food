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

import com.example.manager_food.Adapter.CompletedOrdersAdapter;
import com.example.manager_food.CompletedOrderActivity;
import com.example.manager_food.DetailsOrderActivity;
import com.example.manager_food.R;
import com.example.manager_food.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class CompletedOrdersFragment extends Fragment implements CompletedOrdersAdapter.OnOrderClickListener {

    private RecyclerView recyclerViewOrders;
    private CompletedOrdersAdapter completedOrderAdapter;
    private List<OrderItem> orderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_orders, container, false);

        initializeViews(view);
        setupRecyclerView();

        if (getArguments() != null) {
            orderList = getArguments().getParcelableArrayList("orders");
            orderList = filterCompletedOrders(orderList);
        } else {
            orderList = new ArrayList<>();
        }

        completedOrderAdapter = new CompletedOrdersAdapter(getContext(), orderList, this::onOrderClick);
        recyclerViewOrders.setAdapter(completedOrderAdapter);

        return view;
    }

    private void initializeViews(View view) {
        recyclerViewOrders = view.findViewById(R.id.recyclerViewCompleted);
    }

    private void setupRecyclerView() {
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private List<OrderItem> filterCompletedOrders(List<OrderItem> orders) {
        List<OrderItem> completedOrders = new ArrayList<>();
        if (orders != null) {
            for (OrderItem order : orders) {
                if ("مكتملة".equals(order.getOrderStatus())) { // Arabic for "Completed"
                    completedOrders.add(order);
                }
            }
        }
        return completedOrders;
    }

    public static CompletedOrdersFragment newInstance(List<OrderItem> orders) {
        CompletedOrdersFragment fragment = new CompletedOrdersFragment();
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
