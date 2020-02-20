package com.example.switchme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    // Change the IP ADDRESS HERE!!! Set it to Macbook's IP Address
    public static final String URL = "http://192.168.0.103/SwitchMe";
    private EditText etUsername;
    private EditText etPassword;
    private Button   btnSubmit;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        etUsername = findViewById(R.id.username_et);
        etUsername.setText("admin");
        etPassword = findViewById(R.id.password_et);
        etPassword.setText("pass");
        btnSubmit = findViewById(R.id.login_button);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etUsername.getText().toString().equals("admin")) {
                    new AlertDialog.Builder(context)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Invalid Credentials")
                    .setMessage("Your username is incorrect!")
                    .show();
                    return;
                }

                if (!etPassword.getText().toString().equals("pass")) {
                    new AlertDialog.Builder(context)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("Invalid Credentials")
                            .setMessage("Your password is incorrect!")
                            .show();
                    return;
                }

                Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(homeIntent);
            }
        });
    }
}
