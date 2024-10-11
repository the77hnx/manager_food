package com.example.manager_food;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager_food.Adapter.CategoryAdapter;
import com.example.manager_food.Adapter.ItemsAdapter;
import com.example.manager_food.model.Category;
import com.example.manager_food.model.Item;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowShopDetailsActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String OFFER_STATUS_KEY = "offer_status";
    private static final String updateStoreStatusURL = "http://192.168.1.33/fissa/Manager/Status_Magasin.php";
    private static final String fetchStoreDataURL = "http://192.168.1.33/fissa/Manager/Fetch_Magasin_Information.php";

    private RecyclerView recyclerViewCat;
    private RecyclerView recyclerViewItem;
    private CategoryAdapter categoryAdapter;
    private ItemsAdapter itemsAdapter;
    private Switch offerSwitch;
    private TextView shopNameTextView, offerStatusTextView, status_det;
    private Button addCategoryButton, addProductButton;

    private List<Item> itemList;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shop_details);

        // Initialize UI elements
        recyclerViewCat = findViewById(R.id.recycler_view_cat);
        recyclerViewItem = findViewById(R.id.recycler_view_item);
        offerSwitch = findViewById(R.id.offer_switch_details);
        shopNameTextView = findViewById(R.id.shopname_det);
        offerStatusTextView = findViewById(R.id.status_det);
        addCategoryButton = findViewById(R.id.addcategory);
        addProductButton = findViewById(R.id.addproduct);
        status_det = findViewById(R.id.status_det);
        // Load the saved offer switch state
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean offerStatus = preferences.getBoolean(OFFER_STATUS_KEY, false);

        // Retrieve the offer switch status from the Intent (if passed)
        Intent intent = getIntent();
        if (intent.hasExtra("OFFER_STATUS")) {
            offerStatus = intent.getBooleanExtra("OFFER_STATUS", false);
        }

        // Set the offerSwitch state and update the offerStatusTextView
        offerSwitch.setChecked(offerStatus);
        updateOfferStatusText(offerStatus);

        // Set up listener for the offerSwitch to sync with offerStatusTextView
        offerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateOfferStatusText(isChecked);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(OFFER_STATUS_KEY, isChecked);
            editor.apply();
        });

        // Initialize category and item lists
        categoryList = new ArrayList<>();
        itemList = new ArrayList<>();

        // Initialize adapters
        categoryAdapter = new CategoryAdapter(categoryList, this);
        itemsAdapter = new ItemsAdapter(itemList, this);

        // Setup RecyclerViews
        setupRecyclerViews();

        // Set up click listeners for buttons
        addCategoryButton.setOnClickListener(v -> startActivity(new Intent(ShowShopDetailsActivity.this, AddNewCategoryActivity.class)));
        addProductButton.setOnClickListener(v -> {
            Intent addProductIntent = new Intent(ShowShopDetailsActivity.this, AddNewItemActivity.class);
            addProductIntent.putStringArrayListExtra("CATEGORIES_LIST", getCategoryNames());
            startActivity(addProductIntent);
        });

        // Setup bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_ssd);
        bottomNavigationView.setSelectedItemId(R.id.navigation_following); // Change this based on the activity

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Navigate to ShopsActivity
                    Intent intent = new Intent(ShowShopDetailsActivity.this, ShopMainActivity.class);
                    intent.putExtra("OFFER_STATUS", offerSwitch.isChecked());
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.navigation_following) {
                    // Show toast indicating following action
                    startActivity(new Intent(ShowShopDetailsActivity.this, ShowShopDetailsActivity.class));

                    return true;
                } else if (itemId == R.id.navigation_basket) {
                    // Navigate to OrderSummaryActivity
                    startActivity(new Intent(ShowShopDetailsActivity.this, OurOrdersActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(ShowShopDetailsActivity.this, EditProfileActivity.class));
                    return true;
                }
                return false;
            }
        });


        // Fetch data from server
        fetchDataFromServer();
    }

    private void updateOfferStatusText(boolean isChecked) {
        if (isChecked) {
            offerStatusTextView.setText("مفتوح");
            offerStatusTextView.setTextColor(getResources().getColor(R.color.green));
        } else {
            offerStatusTextView.setText("مغلق");
            offerStatusTextView.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private void setupRecyclerViews() {
        recyclerViewCat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCat.setAdapter(categoryAdapter);

        recyclerViewItem.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItem.setAdapter(itemsAdapter);
    }

    private void fetchDataFromServer() {
        String url = "http://192.168.1.33/fissa/Manager/Fetch_Cat_Prod.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        categoryList.clear();
                        itemList.clear();
                        Log.d("Response", String.valueOf(response));

                        JSONArray categories = response.getJSONArray("categories");
                        JSONArray products = response.getJSONArray("products");

                        parseCategories(categories);
                        parseProducts(products);

                        categoryAdapter.notifyDataSetChanged();
                        itemsAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.e("Error parsing data", e.getMessage());
                        Toast.makeText(ShowShopDetailsActivity.this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Error fetching data", error.toString());
                    Toast.makeText(ShowShopDetailsActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void parseCategories(JSONArray categories) throws JSONException {
        for (int i = 0; i < categories.length(); i++) {
            JSONObject categoryJson = categories.getJSONObject(i);
            Category category = new Category(categoryJson.getInt("Id_Cat"), categoryJson.getString("Nom_Cat"));
            categoryList.add(category);
        }
    }

    private void parseProducts(JSONArray products) throws JSONException {
        for (int j = 0; j < products.length(); j++) {
            JSONObject productJson = products.getJSONObject(j);
            Item item = new Item(
                    productJson.getInt("Id_Prod"),
                    productJson.getString("Nom_Prod"),
                    productJson.getDouble("Prix_prod"),
                    productJson.optString("Desc_prod", "No Description"), // Default description
                    productJson.getInt("Id_Cat")
            );
            itemList.add(item);
        }
    }

    private ArrayList<String> getCategoryNames() {
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : categoryList) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }



    private void showStoreStatus() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(fetchStoreDataURL); // Use the URL to fetch the store data
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                    return result.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        // Fetch store status
                        String status = jsonObject.optString("Statut_magasin");
                        updateStoreStatusUI(status); // Call method to update the UI
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ShowShopDetailsActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ShowShopDetailsActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void updateStoreStatus(boolean isOpen) {
        new AsyncTask<Boolean, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Boolean... params) {
                try {
                    URL url = new URL(updateStoreStatusURL); // URL to update the store status
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String data = "statut_magasin=" + (params[0] ? "مفتوح" : "مغلق");

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    writer.write(data);
                    writer.flush();
                    writer.close();

                    int responseCode = connection.getResponseCode();

                    Log.d("Update Store Status", "Sending status: " + (params[0] ? "مفتوح" : "مغلق"));
                    return responseCode == HttpURLConnection.HTTP_OK;

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Error message", e.getMessage());
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    Toast.makeText(ShowShopDetailsActivity.this, "Store status updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShowShopDetailsActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(isOpen);
    }


    private void updateStoreStatusUI(String status) {
        // Update the UI based on the store status
        if ("مفتوح".equals(status)) {
            offerSwitch.setChecked(true);
            updateBasketText(true); // Update basket text to "مفتوح"
        } else {
            offerSwitch.setChecked(false);
            updateBasketText(false); // Update basket text to "مغلق"
        }
    }

    // Method to update the basket TextView based on the Switch state
    private void updateBasketText(boolean isChecked) {
        if (isChecked) {
            status_det.setText("مفتوح");
            status_det.setTextColor(getResources().getColor(R.color.green));
        } else {
            status_det.setText("مغلق");
            status_det.setTextColor(getResources().getColor(R.color.red));
        }
    }
}
