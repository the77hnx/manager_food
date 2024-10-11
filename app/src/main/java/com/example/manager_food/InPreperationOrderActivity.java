package com.example.manager_food;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.manager_food.Adapter.OrderItemsAdapter;
import com.example.manager_food.model.OrderItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InPreperationOrderActivity extends AppCompatActivity {

    // Declare TextViews at the class level
    private TextView customerNameTextView;
    private TextView customerPhoneTextView;
    private TextView customerEmailTextView;
    private TextView customerAddressTextView;
    private TextView MessgaeMagasinTextView;
    private TextView OrderPriceTextView;
    private TextView DeliveryPriceTextView;
    private TextView AllPriceTextView;
    private TextView OrderDateTextView;
    private TextView OrderIDTextView;
    private TextView OrderStatusTextView;
    private Button DonePrep , CancelOrder;
    private RecyclerView recyclerView;
    private OrderItemsAdapter orderItemsAdapter;
    private List<OrderItems> orderItemsList; // Change to List of OrderItems
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_in_preperation_order);

        // Extract order details from Intent
        String orderId = getIntent().getStringExtra("orderId");
        Log.d("NewOrderActivity", "Received orderId: " + orderId);
        customerNameTextView = findViewById(R.id.customer_name_in_preperation_order_det);
        customerPhoneTextView = findViewById(R.id.numbertv_in_preperation_order_det);
        customerEmailTextView = findViewById(R.id.emailtv_in_preperation_order_det);
        customerAddressTextView = findViewById(R.id.addresstv_in_preperation_order_det);
        MessgaeMagasinTextView = findViewById(R.id.order_message_in_preperation_order_det);
        OrderPriceTextView = findViewById(R.id.totalTextView_in_preperation_order_det);
        DeliveryPriceTextView = findViewById(R.id.deliveryPriceTextView_in_preperation_order_det);
        AllPriceTextView = findViewById(R.id.totalWithDeliveryTextView_in_preperation_order_det);
        OrderDateTextView = findViewById(R.id.order_date_in_preperation_order_det);
        OrderIDTextView = findViewById(R.id.order_total_in_preperation_order_det);
        OrderStatusTextView = findViewById(R.id.order_status_in_preperation_order_det);
        DonePrep = findViewById(R.id.done_prep_order_det);
        CancelOrder = findViewById(R.id.cancel_order_in_preperation_det);

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recycler_view_inpreparation_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the items list and adapter
        orderItemsList = new ArrayList<>();
        orderItemsAdapter = new OrderItemsAdapter(orderItemsList);
        recyclerView.setAdapter(orderItemsAdapter);

        sendOrderIdToPHP(orderId);
        getOrderDetailsFromPHP(orderId);


        DonePrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call function to update order status to 2 (Accepted)
                updateOrderStatus(orderId, 3);
            }
        });

        CancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call function to update order status to 5 (Cancelled)
                updateOrderStatus(orderId, 5);
            }
        });
    }

    private void sendOrderIdToPHP(String orderId) {
        String url = "http://192.168.1.33/fissa/Manager/Details_Order.php?orderId=" + orderId;

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a StringRequest to send the orderId
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log or handle the server response if needed
                        Log.d("PHP Response", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Log.e("Volley Error", error.toString());
            }
        });

        // Add the request to the queue
        requestQueue.add(stringRequest);
    }
    private void getOrderDetailsFromPHP(String orderId) {
        String url = "http://192.168.1.33/fissa/Manager/Details_Order.php?orderId=" + orderId;

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a JSON request to get order details
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract order details from the JSON response
                            JSONObject order = response.getJSONObject("order");
                            String orderPrice = order.getString("orderPrice");
                            String deliveryPrice = order.getString("deliveryPrice");
                            String orderDate = order.getString("orderDate");
                            String restaurantMessage = order.getString("restaurantMessage");

                            // Get customer details
                            JSONObject customer = order.getJSONObject("customer");
                            String customerName = customer.getString("customerName");
                            String customerNumber = customer.getString("customerNumber");
                            String customerEmail = customer.getString("customerEmail");
                            String customerCoordinates = customer.getString("customerCoordinates");

                            // Get order status
                            String orderStatus = order.getString("orderStatus");


                            customerNameTextView.setText(customerName);
                            customerPhoneTextView.setText(customerNumber);
                            customerEmailTextView.setText(customerEmail);
                            customerAddressTextView.setText(customerCoordinates);
                            MessgaeMagasinTextView.setText(restaurantMessage);
                            OrderPriceTextView.setText( " سعر الطلب : " + orderPrice);
                            DeliveryPriceTextView.setText( " سعر التوصيل : " + deliveryPrice);
                            AllPriceTextView.setText(String.valueOf( " السعر الكلي : " + (orderPrice + deliveryPrice) ));
                            OrderDateTextView.setText( " تاريخ الطلب : " + orderDate);
                            OrderIDTextView.setText( " رقم الطلب : " + orderId);
                            OrderStatusTextView.setText( " حالة الطلب : " + orderStatus);

                            // Get order items
                            JSONArray items = order.getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject item = items.getJSONObject(i);
                                String itemName = item.getString("itemName");
                                int itemQuantity = item.getInt("itemQuantity");
                                double itemPrice = item.getDouble("itemPrice");

                                // Create OrderItems object and add to the list
                                OrderItems orderItem = new OrderItems(itemName, itemPrice, itemQuantity);
                                orderItemsList.add(orderItem);  // Add each item to the list
                                // You can now use these details to display in your app
                                Log.d("Order Item", "Name: " + itemName + ", Quantity: " + itemQuantity + ", Price: " + itemPrice);
                            }


                            // Notify the adapter that the data has changed
                            orderItemsAdapter.notifyDataSetChanged();

                            // Display other details as needed
                            Log.d("Order Details", "Price: " + orderPrice + ", Delivery Price: " + deliveryPrice);
                            Log.d("Customer Details", "Name: " + customerName + ", Email: " + customerEmail);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Eroor", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Log.e("Volley Error", error.toString());
            }
        });

        // Add the request to the queue
        requestQueue.add(jsonObjectRequest);
    }
    private void updateOrderStatus(String orderId, int newStatus) {
        String url = "http://192.168.1.33/fissa/Manager/Update_Status_cmd.php";

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a StringRequest to send the orderId and newStatus via POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log or handle the server response
                        Log.d("PHP Response", response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("message")) {
                                Log.d("Order Update", "Success: " + jsonResponse.getString("message"));
                                Intent intent = new Intent(InPreperationOrderActivity.this , OurOrdersActivity.class);
                                startActivity(intent);
                            } else if (jsonResponse.has("error")) {
                                Log.e("Order Update", "Error: " + jsonResponse.getString("error"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Log.e("Volley Error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Send the orderId and newStatus as POST parameters
                Map<String, String> params = new HashMap<>();
                params.put("orderId", orderId);
                params.put("status", String.valueOf(newStatus));
                return params;
            }
        };

        // Add the request to the queue
        requestQueue.add(stringRequest);
    }

}