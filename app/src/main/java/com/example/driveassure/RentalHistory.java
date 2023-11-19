package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;


public class RentalHistory extends AppCompatActivity {
    private static final String[] FILTER_OPTIONS = {"All", "Today", "Yesterday", "LastWeek", "LastMonth"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        ImageButton backbtn = findViewById(R.id.backBtns);
//        backbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), profileUserFragment.class);
//                startActivity(intent);
//            }
//        });

        setContentView(R.layout.activity_rental_history);
        Spinner filterSpinner = findViewById(R.id.rentalHistorySpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FILTER_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedFilter = FILTER_OPTIONS[position];
                fetchDataBasedOnFilter(selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


    }


    private void fetchDataBasedOnFilter(String selectedFilter) {
        System.out.println("Fetching data for filter: " + selectedFilter);
    }
}
