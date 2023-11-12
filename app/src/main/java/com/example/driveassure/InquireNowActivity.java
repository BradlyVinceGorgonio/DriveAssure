package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class InquireNowActivity extends AppCompatActivity {

    Button datePickerButton;
    Button timeStartedFrom;
    Button timeFinishedFrom;
    Button datePickerFromButton;
    Button datePickerUntilButton;

    String selectedDateFrom;
    String selectedDateUntil;
    String startedTime;
    String finishedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire_now);

        datePickerFromButton = findViewById(R.id.datePickerFromButton);

        datePickerFromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        datePickerUntilButton = findViewById(R.id.datePickerUntilButton);

        datePickerUntilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog1();
            }
        });

        timeStartedFrom = findViewById(R.id.timeStartedFrom);

        timeStartedFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        timeFinishedFrom = findViewById(R.id.timeFinishedFrom);

        timeFinishedFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog1();
            }
        });

    }
    private void showDatePickerDialog() {
        final DatePicker datePicker = new DatePicker(this);

        // Create a DatePickerDialog with the current date as the default
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Format the selected date as a string (YEAR/MONTH/DAY)
                        selectedDateFrom = String.format("%04d/%02d/%02d", year, monthOfYear + 1, dayOfMonth);
                        datePickerButton.setText(selectedDateFrom); // Display the selected date on the button
                    }
                },
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth()
        );
        datePickerDialog.show();
    }
    private void showDatePickerDialog1() {
        final DatePicker datePicker = new DatePicker(this);

        // Create a DatePickerDialog with the current date as the default
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Format the selected date as a string (YEAR/MONTH/DAY)
                        selectedDateUntil = String.format("%04d/%02d/%02d", year, monthOfYear + 1, dayOfMonth);
                        datePickerButton.setText(selectedDateUntil); // Display the selected date on the button
                    }
                },
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth()
        );
        datePickerDialog.show();
    }
    private void showTimePickerDialog() {
        // Get the current time
        final java.util.Calendar currentTime = java.util.Calendar.getInstance();
        int currentHour = currentTime.get(java.util.Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(java.util.Calendar.MINUTE);

        // Create a TimePickerDialog with the current time as the default
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Format the selected time as a string
                        startedTime = String.format("%02d:%02d", hourOfDay, minute);
                        timeStartedFrom.setText(startedTime); // Display the selected time on the button
                    }
                },
                currentHour,
                currentMinute,
                true // Use 24-hour format
        );
        timePickerDialog.show();
    }

    private void showTimePickerDialog1() {
        // Get the current time
        final java.util.Calendar currentTime = java.util.Calendar.getInstance();
        int currentHour = currentTime.get(java.util.Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(java.util.Calendar.MINUTE);

        // Create a TimePickerDialog with the current time as the default
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Format the selected time as a string
                        finishedTime = String.format("%02d:%02d", hourOfDay, minute);
                        timeFinishedFrom.setText(finishedTime); // Display the selected time on the button
                    }
                },
                currentHour,
                currentMinute,
                true // Use 24-hour format
        );
        timePickerDialog.show();
    }
}