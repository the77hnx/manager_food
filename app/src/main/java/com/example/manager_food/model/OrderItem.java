package com.example.manager_food.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem implements Parcelable {
    private String customerName;
    private String orderDate;
    private String orderId;
    private String orderTotal;
    private String itemName;
    private String itemQuantity;
    private String itemPrice;
    private String orderMessage;
    private String orderStatus;

    // Constructor
    public OrderItem(String customerName, String orderDate, String orderId, String orderTotal,
                     String itemName, String itemQuantity, String itemPrice, String orderMessage,
                     String orderStatus) {
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.orderTotal = orderTotal;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.orderMessage = orderMessage;
        this.orderStatus = orderStatus;
    }

    // Parcelable implementation
    protected OrderItem(Parcel in) {
        customerName = in.readString();
        orderDate = in.readString();
        orderId = in.readString();
        orderTotal = in.readString();
        itemName = in.readString();
        itemQuantity = in.readString();
        itemPrice = in.readString();
        orderMessage = in.readString();
        orderStatus = in.readString();
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerName);
        dest.writeString(orderDate);
        dest.writeString(orderId);
        dest.writeString(orderTotal);
        dest.writeString(itemName);
        dest.writeString(itemQuantity);
        dest.writeString(itemPrice);
        dest.writeString(orderMessage);
        dest.writeString(orderStatus);
    }

    // Getters
    public String getCustomerName() { return customerName; }
    public String getOrderDate() { return orderDate; }
    public String getOrderId() { return orderId; }
    public String getOrderTotal() { return orderTotal; }
    public String getItemName() { return itemName; }
    public String getItemQuantity() { return itemQuantity; }
    public String getItemPrice() { return itemPrice; }
    public String getOrderMessage() { return orderMessage; }
    public String getOrderStatus() { return orderStatus; }
}
