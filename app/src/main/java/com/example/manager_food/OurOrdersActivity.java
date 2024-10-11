package com.example.manager_food;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.manager_food.Fragement.AwaitingDeliveryOrdersFragment;
import com.example.manager_food.Fragement.CancelledOrdersFragment;
import com.example.manager_food.Fragement.CompletedOrdersFragment;
import com.example.manager_food.Fragement.InDeliveryOrdersFragment;
import com.example.manager_food.Fragement.InPreparationOrdersFragment;
import com.example.manager_food.Fragement.NewOrdersFragment;
import com.example.manager_food.model.OrderItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class OurOrdersActivity extends AppCompatActivity {

    private List<OrderItem> case0Orders = new ArrayList<>();
    private List<OrderItem> case1Orders = new ArrayList<>();
    private List<OrderItem> case2Orders = new ArrayList<>();
    private List<OrderItem> case3Orders = new ArrayList<>();
    private List<OrderItem> case4Orders = new ArrayList<>();
    private List<OrderItem> case5Orders = new ArrayList<>();
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_orders);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        fetchOrdersFromServer(); // Fetch orders from the server

        setupViewPager(viewPager); // Set up the ViewPager with the adapter
        setupTabLayout(tabLayout, viewPager); // Set up the TabLayout with the ViewPager
    }

    private void setupViewPager(ViewPager2 viewPager) {
        pagerAdapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return NewOrdersFragment.newInstance(case0Orders);
                    case 1:
                        return InPreparationOrdersFragment.newInstance(case1Orders);
                    case 2:
                        return InDeliveryOrdersFragment.newInstance(case2Orders);
                    case 3:
                        return AwaitingDeliveryOrdersFragment.newInstance(case3Orders);
                    case 4:
                        return CompletedOrdersFragment.newInstance(case4Orders);
                    case 5:
                        return CancelledOrdersFragment.newInstance(case5Orders);
                    default:
                        return new Fragment();
                }
            }

            @Override
            public int getItemCount() {
                return 6; // Number of cases/tabs
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }

    private void setupTabLayout(TabLayout tabLayout, ViewPager2 viewPager) {
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("جديد");
                    break;
                case 1:
                    tab.setText("قيد التحضير");
                    break;
                case 2:
                    tab.setText("قيد التوصيل");
                    break;
                case 3:
                    tab.setText("في انتظار التسليم");
                    break;
                case 4:
                    tab.setText("مكتملة");
                    break;
                case 5:
                    tab.setText("ملغية");
                    break;
            }
        }).attach();
    }

    private void fetchOrdersFromServer() {
        // Simulate fetching orders and populating the lists like case0Orders, case1Orders, etc.
    }
}
