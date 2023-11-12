package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;

public class InquireNowActivity extends AppCompatActivity {


    Button timeStartedFrom;
    Button timeFinishedFrom;
    Button datePickerFromButton;
    Button datePickerUntilButton;

    String selectedDateFrom = "";
    String selectedDateUntil = "";
    String startedTime = "";
    String finishedTime = "";
    String pickUpArea = "";
    String returnArea = "";
    Button submitBtn;

    EditText pickUpLocation;
    EditText returnLocation;

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

        pickUpLocation = findViewById(R.id.pickUpLocation);
        returnLocation = findViewById(R.id.returnLocation);

        submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setEnabled(false);
        submitBtn.setBackgroundColor(getResources().getColor(R.color.disabledGrey));

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ANOORAS", "started: " + startedTime);
                Log.d("ANOORAS", "finished: " + finishedTime);
                Log.d("ANOORAS", "selected Date From : " + selectedDateFrom);
                Log.d("ANOORAS", "selected Date Until : " + selectedDateUntil);

                pickUpArea = pickUpLocation.getText().toString();
                returnArea = returnLocation.getText().toString();

                Log.d("ANOORAS", "Pickup Location : " + pickUpArea);
                Log.d("ANOORAS", "Return Location : " + returnArea);

                Intent intent = new Intent(InquireNowActivity.this, IdVerification.class);
                // Add the data to the intent
                intent.putExtra("SELECTED_DATE_FROM", selectedDateFrom);
                intent.putExtra("SELECTED_DATE_UNTIL", selectedDateUntil);
                intent.putExtra("STARTED_TIME", startedTime);
                intent.putExtra("FINISHED_TIME", finishedTime);
                intent.putExtra("PICK_UP_AREA", pickUpArea);
                intent.putExtra("RETURN_AREA", returnArea);
                startActivity(intent);

            }
        });

        pickUpLocation.addTextChangedListener(textWatcher);
        returnLocation.addTextChangedListener(textWatcher);

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Not needed for this example
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Not needed for this example
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Update the state of the submit button
            updateSubmitButtonState();
        }
    };

    // Method to update the state of the submit button
    private void updateSubmitButtonState() {
        // Check if all required fields are filled and buttons are pressed
        boolean allFieldsFilled =
                !pickUpLocation.getText().toString().isEmpty()
                && !returnLocation.getText().toString().isEmpty()
                && !startedTime.isEmpty()  // Assuming startedTime and finishedTime are updated in the time picker dialogs
                && !finishedTime.isEmpty()
                && !selectedDateFrom.isEmpty()
                && !selectedDateUntil.isEmpty();

        // Enable or disable the submit button based on the condition

        submitBtn.setEnabled(allFieldsFilled);

        if(allFieldsFilled)
        {
            submitBtn.setBackgroundColor(getResources().getColor(R.color.blue));
        }
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
                        datePickerFromButton.setText(selectedDateFrom); // Display the selected date on the button
                        updateSubmitButtonState();
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
                        datePickerUntilButton.setText(selectedDateUntil); // Display the selected date on the button
                        updateSubmitButtonState();
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
                        updateSubmitButtonState();
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
                        updateSubmitButtonState();
                    }
                },
                currentHour,
                currentMinute,
                true // Use 24-hour format
        );
        timePickerDialog.show();


    }
}