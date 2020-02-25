package com.example.switchme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    /**
     * Funciton to start intent for room1 activity
     */
    public void startRoom1(View v) {
        Intent n = new Intent(getApplicationContext(), HomeActivityRelay1.class);
        startActivity(n);
    }

    /**
     * Funciton to start intent for room2 activity
     */
    public void startRoom2(View v) {
        Intent n = new Intent(getApplicationContext(), HomeActivityRelay2.class);
        startActivity(n);
    }

    /**
     * Funciton to start intent for room3 activity
     */
    public void startRoom3(View v) {
        Intent n = new Intent(getApplicationContext(), HomeActivityRelay3.class);
        startActivity(n);
    }

    /**
     * Funciton to start intent for logs activity
     */
    public void startLogs(View v) {
        Intent n = new Intent(getApplicationContext(), LogsActivity.class);
        startActivity(n);
    }

    /**
     * Funciton to start intent for consumption and billing activity
     */
    public void startConsumptionAndBilling(View v) {
        Intent n = new Intent(getApplicationContext(), HomeActivityRelay1.class);
        startActivity(n);
    }

}
