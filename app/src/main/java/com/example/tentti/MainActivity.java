package com.example.tentti;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final double WEEKDAY_BASE_FEE = 4.50;
    private static final double HOLIDAY_BASE_FEE = 7.50;
    private static final double PRICE_PER_KM_1_4 = 1.90;
    private static final double PRICE_PER_KM_5_8 = 2.50;

    private static final int MAX_DISTANCE_KM = 150;
    private static final int MAX_PASSENGERS = 8;

    private NumberPicker distancePicker;
    private NumberPicker passengerCountPicker;
    private MaterialSwitch holidaySwitch;
    private TextView priceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        distancePicker = findViewById(R.id.distance_picker);
        passengerCountPicker = findViewById(R.id.passenger_count_picker);
        holidaySwitch = findViewById(R.id.holiday_switch);
        priceText = findViewById(R.id.price_text);

        initDistancePicker();
        initPassengerPicker();

        holidaySwitch.setOnCheckedChangeListener((button, isChecked) -> updatePrice());

        updatePrice();
    }

    private void initDistancePicker() {
        distancePicker.setMinValue(0);
        distancePicker.setMaxValue(MAX_DISTANCE_KM);
        distancePicker.setWrapSelectorWheel(true);
        distancePicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        String[] distanceLabels = new String[MAX_DISTANCE_KM + 1];
        for (int i = 0; i <= MAX_DISTANCE_KM; i++) {
            distanceLabels[i] = i + " km";
        }
        distancePicker.setDisplayedValues(distanceLabels);
        distancePicker.setValue(0);

        distancePicker.setOnValueChangedListener(
                (picker, oldVal, newVal) -> updatePrice());
    }

    private void initPassengerPicker() {
        passengerCountPicker.setMinValue(0);
        passengerCountPicker.setMaxValue(MAX_PASSENGERS);
        passengerCountPicker.setWrapSelectorWheel(true);
        passengerCountPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        passengerCountPicker.setValue(0);

        passengerCountPicker.setOnValueChangedListener(
                (picker, oldVal, newVal) -> updatePrice());
    }

    private void updatePrice() {
        int distance = distancePicker.getValue();
        int passengerCount = passengerCountPicker.getValue();
        boolean isHoliday = holidaySwitch.isChecked();

        if (distance == 0 || passengerCount == 0) {
            priceText.setText("");
            return;
        }

        double baseFee = isHoliday ? HOLIDAY_BASE_FEE : WEEKDAY_BASE_FEE;
        double pricePerKm = passengerCount <= 4
                ? PRICE_PER_KM_1_4
                : PRICE_PER_KM_5_8;
        double totalPrice = baseFee + distance * pricePerKm;

        DecimalFormat priceFormat = new DecimalFormat(
                "0.0#", new DecimalFormatSymbols(new Locale("fi")));
        priceText.setText(priceFormat.format(totalPrice) + " €");
    }
}
