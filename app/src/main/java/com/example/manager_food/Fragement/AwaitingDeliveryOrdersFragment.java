package com.example.manager_food.Fragement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.manager_food.Adapter.AwaitingDeliveryOrdersAdapter;
import com.example.manager_food.AwaitingDeliveryOrderActivity;
import com.example.manager_food.NewOrderActivity;
import com.example.manager_food.R;
import com.example.manager_food.model.OrderItem;
import com.example.manager_food.model.OrderItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AwaitingDeliveryOrdersFragment extends Fragment {

    private RecyclerView recyclerViewAwaitingDeliveryOrders;
    private AwaitingDeliveryOrdersAdapter awaitingDeliveryOrdersAdapter;
    private List<OrderItem> orderList;

    private TextView namecategory;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_awaiting_delivery_orders, container, false);

        initializeViews(view);
        setupRecyclerView();

        requestQueue = Volley.newRequestQueue(getContext());
        fetchOrders();

        return view;
    }

    private void initializeViews(View view) {
        namecategory = view.findViewById(R.id.cat_text5);
        recyclerViewAwaitingDeliveryOrders = view.findViewById(R.id.recyclerViewAwaitingDelivery);
    }

    private void setupRecyclerView() {
        recyclerViewAwaitingDeliveryOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();
        awaitingDeliveryOrdersAdapter = new AwaitingDeliveryOrdersAdapter(getContext(), orderList, order -> {
            // Handle order click here if needed
            Intent intent = new Intent(getContext(), AwaitingDeliveryOrderActivity.class);
            intent.putExtra("orderId", order.getOrderId());  // Pass the orderId to the next activity
            startActivity(intent);
        });
        recyclerViewAwaitingDeliveryOrders.setAdapter(awaitingDeliveryOrdersAdapter);
    }
    public static AwaitingDeliveryOrdersFragment newInstance(List<OrderItem> orders) {
        AwaitingDeliveryOrdersFragment fragment = new AwaitingDeliveryOrdersFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("orders", new ArrayList<>(orders));
        fragment.setArguments(args);
        return fragment;
    }
    private void fetchOrders() {
        String url = "http://192.168.1.33/fissa/Manager/Fetch_Orders.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray ordersArray = response.getJSONArray("case3"); // Fetching orders with case 3
                            Log.d("Server Response", String.valueOf(ordersArray));

                            for (int i = 0; i < ordersArray.length(); i++) {
                                JSONObject orderObj = ordersArray.getJSONObject(i);

                                String customerName = orderObj.getString("Nom_Client");
                                String orderStatus = orderObj.getString("Nom_Statut");
                                String orderDate = orderObj.getString("Date_commande");
                                String orderTime = orderObj.getString("Heure_commande");
                                double orderTotal = orderObj.getDouble("Prix_Demande");
                                String orderId = orderObj.getString("Id_Demandes");
                                String orderMessage = orderObj.getString("info_mag");
                                int idStatutCommande = orderObj.getInt("Id_Statut_Commande");

                                String itemName = orderObj.getString("Nom_Article");
                                int itemQuantity = orderObj.getInt("Quantite");
                                double itemPrice = orderObj.getDouble("Prix");

                                List<OrderItems> items = new ArrayList<>();
                                items.add(new OrderItems(itemName, itemPrice, itemQuantity));

                                OrderItem orderItem = new OrderItem(customerName, orderDate, orderTime, orderId, orderTotal, orderMessage, orderStatus, idStatutCommande, items);

                                orderList.add(orderItem);

                                Log.d("OrderItem", orderItem.toString());
                            }

                            awaitingDeliveryOrdersAdapter.setOrderList(orderList);
                            awaitingDeliveryOrdersAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Error fetching orders: " + error.getMessage());
            }
        });

        requestQueue.add(request);
    }
}
