package com.example.manager_food;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewOrderActivity extends AppCompatActivity {

    private TextView customerName, orderDate, orderTotal, phoneNumber, email, address, orderMessage, orderTotalAmount, orderStatus;
    private Button callBtn, emailBtn, showAddressBtn, acceptOrderBtn, cancelOrderBtn;
    private boolean isOrderAccepted = false; // Track the acceptance state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        // Initialize UI elements
        customerName = findViewById(R.id.customer_name);
        orderDate = findViewById(R.id.order_date);
        orderTotal = findViewById(R.id.order_total);
        phoneNumber = findViewById(R.id.numbertv);
        email = findViewById(R.id.emailtv);
        address = findViewById(R.id.addresstv);
        orderMessage = findViewById(R.id.order_message);
        orderTotalAmount = findViewById(R.id.totalWithDeliveryTextView);
        orderStatus = findViewById(R.id.order_status); // TextView to display order status

        callBtn = findViewById(R.id.callbtn);
        emailBtn = findViewById(R.id.emailbtn);
        showAddressBtn = findViewById(R.id.showaddressbtn);
        acceptOrderBtn = findViewById(R.id.accept_order);
        cancelOrderBtn = findViewById(R.id.cancel_order);

        // Extract order details from Intent
        Intent intent = getIntent();
        if (intent != null) {
            customerName.setText(intent.getStringExtra("CUSTOMER_NAME"));
            orderDate.setText(intent.getStringExtra("ORDER_DATE"));
            orderTotal.setText(intent.getStringExtra("ORDER_TOTAL"));
            phoneNumber.setText(intent.getStringExtra("PHONE_NUMBER"));
            email.setText(intent.getStringExtra("EMAIL"));
            address.setText(intent.getStringExtra("ADDRESS"));
            orderMessage.setText(intent.getStringExtra("ORDER_MESSAGE"));
            orderTotalAmount.setText(intent.getStringExtra("ORDER_TOTAL_AMOUNT"));
            orderStatus.setText(intent.getStringExtra("ORDER_STATUS"));
        }

        // Set button click listeners
        callBtn.setOnClickListener(v -> {
            // Handle call button click
        });

        emailBtn.setOnClickListener(v -> {
            // Handle email button click
        });

        showAddressBtn.setOnClickListener(v -> {
            // Handle show address button click
        });

        acceptOrderBtn.setOnClickListener(v -> {
            if (!isOrderAccepted) {
                // Change button text and status
                acceptOrderBtn.setText("تم التحضير");
                orderStatus.setText("قيد التحضير");
                orderStatus.setTextColor(Color.parseColor("#FF6347")); // Tomato red
                acceptOrderBtn.setBackgroundColor(Color.parseColor("#FF6347")); // Tomato red
                isOrderAccepted = true; // Update state to accepted
            } else {
                // Change button text and status
                acceptOrderBtn.setText("تم التوصيل");
                orderStatus.setText("في انتظار التسليم");
                orderStatus.setTextColor(Color.parseColor("#FF6347")); // Tomato red
                acceptOrderBtn.setBackgroundColor(Color.GRAY);
                cancelOrderBtn.setBackgroundColor(Color.GRAY);
                acceptOrderBtn.setClickable(false);
                cancelOrderBtn.setClickable(false);
                acceptOrderBtn.setEnabled(false);
                cancelOrderBtn.setEnabled(false);
                isOrderAccepted = false; // Update state to not accepted
            }
        });

        cancelOrderBtn.setOnClickListener(v -> {
            // Handle cancel order button click
            orderStatus.setText("ملغية");
            orderStatus.setTextColor(Color.RED); // Red
            acceptOrderBtn.setVisibility(View.GONE);
            cancelOrderBtn.setVisibility(View.GONE);
        });
    }
}
