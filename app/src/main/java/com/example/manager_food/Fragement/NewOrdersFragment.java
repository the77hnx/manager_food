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
import com.example.manager_food.Adapter.NewOrdersAdapter;
import com.example.manager_food.NewOrderActivity;
import com.example.manager_food.R;
import com.example.manager_food.model.OrderItem;
import com.example.manager_food.model.OrderItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewOrdersFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private NewOrdersAdapter newOrderAdapter;
    private List<OrderItem> orderList;

    private TextView namecategory;
    private RequestQueue requestQueue;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_orders, container, false);

        initializeViews(view);
        setupRecyclerView();

        requestQueue = Volley.newRequestQueue(getContext());
        fetchOrders();

        return view;
    }

    private void initializeViews(View view) {
        namecategory = view.findViewById(R.id.cat_text);
        recyclerViewOrders = view.findViewById(R.id.recyclerViewNewOrders);
    }

    private void setupRecyclerView() {
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();
        newOrderAdapter = new NewOrdersAdapter(getContext(), orderList, order -> {
            // Handle order click here if needed
            Intent intent = new Intent(getContext(), NewOrderActivity.class);
            intent.putExtra("orderId", order.getOrderId());  // Pass the orderId to the next activity
            startActivity(intent);
        });
        recyclerViewOrders.setAdapter(newOrderAdapter);
    }
    public static NewOrdersFragment newInstance(List<OrderItem> orders) {
        NewOrdersFragment fragment = new NewOrdersFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("orders", new ArrayList<>(orders));
        fragment.setArguments(args);
        return fragment;
    }
    private void fetchOrders() {
        String url = "http://192.168.1.35/fissa/Manager/Fetch_Orders.php"; // Replace with your PHP file URL

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray ordersArray = response.getJSONArray("case0"); // Only for status 1 (New)
                            Log.d("Server Response", String.valueOf(ordersArray)); // Log raw server response

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

                                // Since articles are directly included in the same object, we can treat it as one item
                                String itemName = orderObj.getString("Nom_Article");
                                int itemQuantity = orderObj.getInt("Quantite");
                                double itemPrice = orderObj.getDouble("Prix");

                                List<OrderItems> items = new ArrayList<>();
                                items.add(new OrderItems(itemName, itemPrice, itemQuantity));

                                OrderItem orderItem = new OrderItem(customerName, orderDate, orderTime, orderId, orderTotal, orderMessage, orderStatus, idStatutCommande, items);

                                orderList.add(orderItem);

                                Log.d("OrderItem", orderItem.toString());
                                Log.d("orderList", orderList.toString());

                            }

                            Log.d("OrderListSize", "Size: " + orderList.size());
                            newOrderAdapter.setOrderList(orderList);
                            newOrderAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error",e.getMessage());
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
