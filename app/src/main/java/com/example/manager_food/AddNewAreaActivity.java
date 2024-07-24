package com.example.manager_food;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class AddNewAreaActivity extends AppCompatActivity {

    private Spinner stateSpinner, citySpinner;
    private EditText areaEditText;
    private Button addAreaButton, saveChangesButton;
    private ListView deliveryAreasListView;
    private ArrayList<String> deliveryAreasList;
    private ArrayAdapter<String> deliveryAreasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        // Initialize UI components
        stateSpinner = findViewById(R.id.state_spinner);
        citySpinner = findViewById(R.id.city_spinner);
        areaEditText = findViewById(R.id.area_edit_text);
        addAreaButton = findViewById(R.id.add_area_button);
        saveChangesButton = findViewById(R.id.save_changes_button);
        deliveryAreasListView = findViewById(R.id.delivery_areas_list_view);

        // Populate the state and city spinners
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this,
                R.array.states_array, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);

        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        // Initialize the delivery areas list
        deliveryAreasList = new ArrayList<>();
        deliveryAreasAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, deliveryAreasList);
        deliveryAreasListView.setAdapter(deliveryAreasAdapter);
        deliveryAreasListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Add area button click listener
        addAreaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = areaEditText.getText().toString().trim();
                if (!area.isEmpty()) {
                    deliveryAreasList.add(area);
                    deliveryAreasAdapter.notifyDataSetChanged();
                    areaEditText.setText("");
                }
            }
        });

        // Save changes button click listener
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the changes logic here
                // For now, just display a message
                // Toast.makeText(MainActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
