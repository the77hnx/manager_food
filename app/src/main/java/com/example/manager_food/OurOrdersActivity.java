package com.example.manager_food;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.manager_food.Fragement.AllOrdersFragment;
import com.example.manager_food.Fragement.CancelledOrdersFragment;
import com.example.manager_food.Fragement.NewOrdersFragment;
import com.example.manager_food.Fragement.OngoingOrdersFragment;
import com.example.manager_food.Fragement.OnloadingOrdersFragment;
import com.example.manager_food.model.OrderItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class OurOrdersActivity extends AppCompatActivity {
    private List<OrderItem> sampleOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_orders);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        sampleOrders = getSampleOrders();


        // Setup bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_os);
        bottomNavigationView.setSelectedItemId(R.id.navigation_basket); // Change this based on the activity

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Navigate to ShopsActivity
                    Intent intent = new Intent(OurOrdersActivity.this, ShopMainActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.navigation_following) {
                    // Show toast indicating following action
                    startActivity(new Intent(OurOrdersActivity.this, ShowShopDetailsActivity.class));

                    return true;
                } else if (itemId == R.id.navigation_basket) {
                    // Navigate to OrderSummaryActivity
                    startActivity(new Intent(OurOrdersActivity.this, OurOrdersActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(OurOrdersActivity.this, EditProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        FragmentStateAdapter pagerAdapter = new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                // Return the appropriate fragment based on the position
                switch (position) {
//                    case 0:
//                        return AllOrdersFragment.newInstance(sampleOrders);
                    case 0:
                        return NewOrdersFragment.newInstance(sampleOrders);

                    case 1:
                        return OngoingOrdersFragment.newInstance(sampleOrders);
                    case 2:
                        return OnloadingOrdersFragment.newInstance(sampleOrders);
                    case 3:
                        return CancelledOrdersFragment.newInstance(sampleOrders);
                    default:
                        return new Fragment(); // Return a default fragment
                }
            }

            @Override
            public int getItemCount() {
                return 4; // Number of tabs
            }
        };

        viewPager.setAdapter(pagerAdapter);

        // Set up TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
//                        case 0:
//                            tab.setText("الكل");
//                            break;
                        case 0:
                            tab.setText("جديد");
                            break;
                        case 1:
                            tab.setText("قيد التحضير");
                            break;
                        case 2:
                            tab.setText("قيد التسليم");
                            break;
                        case 3:
                            tab.setText("ملغية");
                            break;
                        default:
                            tab.setText("Unknown");
                    }
                }).attach();
    }


    private List<OrderItem> getSampleOrders() {
        List<OrderItem> orders = new ArrayList<>();

        // Example Orders
        orders.add(new OrderItem("سليم فاطمة","21/5/7898", "348",  "42.00 دج",
                "طاكوس", "2", "42.00 دج",
                "الرسالة: مرحباً، الرجاء وضع الصلصة الخضراء في طلبي وأخبر عامل التوصيل أنه يجب أن يأتي إلى الطابق الثاني لأنني لست في المنزل",
                "جديد"
        ));
        orders.add(new OrderItem("سليم فاطمة","21/5/7898", "348",  "42.00 دج",
                "طاكوس", "2", "42.00 دج",
                "الرسالة: مرحباً، الرجاء وضع الصلصة الخضراء في طلبي وأخبر عامل التوصيل أنه يجب أن يأتي إلى الطابق الثاني لأنني لست في المنزل",
                "قيد التحضير"
        ));
        orders.add(new OrderItem("سليم فاطمة","21/5/7898", "348",  "42.00 دج",
                "طاكوس", "2", "42.00 دج",
                "الرسالة: مرحباً، الرجاء وضع الصلصة الخضراء في طلبي وأخبر عامل التوصيل أنه يجب أن يأتي إلى الطابق الثاني لأنني لست في المنزل",
                "قيد التسليم"
        ));
        orders.add(new OrderItem("سليم فاطمة","21/5/7898", "348",  "42.00 دج",
                "طاكوس", "2", "42.00 دج",
                "الرسالة: مرحباً، الرجاء وضع الصلصة الخضراء في طلبي وأخبر عامل التوصيل أنه يجب أن يأتي إلى الطابق الثاني لأنني لست في المنزل",
                "ملغية"
        ));

        return orders;
    }
}



