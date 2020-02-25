package com.example.switchme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HomeActivityRelay2 extends AppCompatActivity {

    private     Context                        context;
    private ToggleButton                   tbSwitch;
    private ImageButton                    tbSchedule;
    private TextView                       tvTimePassed;
    private HomeActivityRelay2.ListUpdater updateList;
    private Calendar                       calendar;
    private ProgressBar                    voltsBar;
    private ProgressBar                    ampsBar;
    private TextView                       voltsText, ampsText, powerUsed, amountGenerated, unitsConsumed;
    private boolean aSyncCancelled;
    private int relayID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_relay_2);

        // Add back-able button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tbSwitch = findViewById(R.id.switcher_tb23);
        tbSchedule = findViewById(R.id.schedule_tb23);
        tvTimePassed = findViewById(R.id.time_passed_tv223);
        voltsBar = findViewById(R.id.volt_progress23);
        ampsBar = findViewById(R.id.amp_progress23);
        voltsText = findViewById(R.id.volt_units_tv233);
        ampsText = findViewById(R.id.amp_units_tv23);
        powerUsed = findViewById(R.id.power_tv3);
        unitsConsumed = findViewById(R.id.units_consumed_tv3);
        amountGenerated = findViewById(R.id.amount_generated_tv3);

        context = this;
        relayID = 2; // Change this to adopt based on relay...

        // Execute Thread
        // Thread to keep updating the list
        aSyncCancelled = false;
        updateList = new HomeActivityRelay2.ListUpdater();
        updateList.execute("");

        calendar = Calendar.getInstance();

        tbSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MainActivity.URL + "/switchState.php";

                Response.Listener listener = new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            //JSONObject authResponse = new JSONObject(response.toString());
                            Log.d("CHECKRESPONSE", "onClick: " + response.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                };

                //Initialize request string with POST method
                String newCheckedState = "";
                if (tbSwitch.isChecked()) {
                    newCheckedState = "ON";
                } else {
                    newCheckedState = "OFF";
                }

                final String finalNewCheckedState = newCheckedState;
                StringRequest request = new StringRequest(Request.Method.POST, url, listener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        //Put user ID and password in data set
                        Calendar t = Calendar.getInstance();
                        param.put("id", Integer.toString(relayID));
                        param.put("state", finalNewCheckedState);
                        param.put("time", "" + t.get(Calendar.YEAR) + "-" + t.get(Calendar.MONTH) + "-" +
                                          t.get(Calendar.DATE) + " " + t.get(Calendar.HOUR) + ":" +
                                          t.get(Calendar.MINUTE) + ":" + t.get(Calendar.SECOND));
                        return param;
                    }
                };
                Volleyton.getInstance(getApplicationContext()).addToRequestQueue(request);
            }
        });


        // Calendar Logic
        tbSchedule.setOnClickListener(new View.OnClickListener() {
            final Calendar dateCalendar = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Setting the Custom View
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.date_input_layout, null);
                final EditText dateInput = view.findViewById(R.id.schedule_date_input);
                final EditText timeInput = view.findViewById(R.id.schedule_time_input);

                // Setting up date calendar showing code
                // Setting up calendar:
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateCalendar.set(Calendar.YEAR, year);
                        dateCalendar.set(Calendar.MONTH, monthOfYear);
                        dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "yyyy-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        dateInput.setText(sdf.format(dateCalendar.getTime()));
                    }
                };
                dateInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateCalendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
                        DatePickerDialog datePicker =
                                new DatePickerDialog(context, date, dateCalendar.get(Calendar.YEAR),
                                                     dateCalendar.get(Calendar.MONTH),
                                                     dateCalendar.get(Calendar.DAY_OF_MONTH));
                        // Setting lower limits
                        DatePicker picker = datePicker.getDatePicker();
                        picker.setMinDate(dateCalendar.getTimeInMillis());

                        datePicker.show();
                    }
                });


                // Setting up time picker showing code
                final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeInput.setText(selectedHour + ":" + selectedMinute + ":00");
                    }
                };
                timeInput.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dateCalendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
                        int hour = dateCalendar.get(Calendar.HOUR_OF_DAY);
                        int minute = dateCalendar.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker = new TimePickerDialog(context, timeSetListener, hour, minute, false);
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    }
                });

                builder.setView(view);

                // Cancel Button
                builder.setTitle("Set Schedule");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Submit Button
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String date = dateInput.getText().toString();
                        final String time = timeInput.getText().toString();

                        // Check if any is empty:
                        if (date.equals("") || time.equals("")) {
                            Toast.makeText(context, "Please enter all two of the fields...", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            return;
                        }

                        final String datetime = date + " " + time;
                        // Sending the request to PHP
                        String url = MainActivity.URL + "/setSchedule.php";
                        Response.Listener listener = new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                try {
                                    JSONObject jsonresponse = new JSONObject(response.toString());
                                    Log.d("CHECKKKK", "onResponse: " + response.toString());
                                    boolean scheduleSuccess = jsonresponse.getBoolean("successful");
                                    if (scheduleSuccess) {
                                        Toast.makeText(context, "Schedule has been set successfully.", Toast.LENGTH_LONG).show();
                                    } else {
                                        String error = jsonresponse.getString("error");
                                        Toast.makeText(context, "Couldn't post schedule: " + error, Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        Response.ErrorListener errorListener = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        };

                        //Initialize request string with POST method
                        StringRequest request = new StringRequest(Request.Method.POST, url, listener, errorListener) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> param = new HashMap<>();
                                param.put("id", Integer.toString(relayID));
                                param.put("datetime", datetime);
                                Log.d("CHECKKKK2", "getParams: " + datetime);
                                return param;
                            }
                        };
                        Volleyton.getInstance(getApplicationContext()).addToRequestQueue(request);

                    }
                });

                // Show dialog
                builder.show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        updateList.cancel(true);
        aSyncCancelled = true;
        super.onDestroy();
    }

    public void updateTimePassed(String m) {
        tvTimePassed.setText(m);
    }

    public void switchTB(boolean b) {
        tbSwitch.setChecked(b);
    }

    public void updateCalendar(long secondstoadd) {
        calendar.set(Calendar.YEAR, 0);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.SECOND, (int) secondstoadd);
    }

    // This class will contain the functionality use to update the list
    private class ListUpdater extends AsyncTask<String, String, String> {

        private String newTime = "Ahan";

        /**
         * Fucntion that will run in the background simultaneously
         */
        @Override
        protected String doInBackground(String... strings) {
            boolean infinite = true;
            boolean firstTime = true;

            // The infinite loop that will keep running and check for updates
            while (infinite) {
                String url = MainActivity.URL + "/getState.php";
                Response.Listener listener = new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject jsonresponse = new JSONObject(response.toString());
                            String state = jsonresponse.getString("colState");
                            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd' 'hh:mm:ss");
                            Date updated = myFormat.parse(jsonresponse.getString("updateTime"));

                            if (state.equals("OFF")) {
                                updateTimePassed("Switch is OFF.");
                                switchTB(false);

                                // Update States
                                voltsBar.setProgress(0);
                                voltsText.setText(0 + "V");
                                ampsBar.setProgress(0);
                                ampsText.setText(0 + " A");
                                powerUsed.setText(0 + " Watts");
                                unitsConsumed.setText(0 + " KWh");
                                amountGenerated.setText(0.00 + " Rs.");
                            } else {
                                switchTB(true);
                                long diff =
                                        Calendar.getInstance().getTime().getTime() - Math.abs(updated.getTime());
                                updateCalendar(TimeUnit.MILLISECONDS.toSeconds(diff));
                                String message = "" + (calendar.get(Calendar.HOUR) < 10 ?
                                                       "0" + calendar.get(Calendar.HOUR) :
                                                       calendar.get(Calendar.HOUR)) + " Hours | " +
                                                 calendar.get(Calendar.MINUTE) + " Minutes | " +
                                                 calendar.get(Calendar.SECOND) + " Seconds";
                                updateTimePassed(message);

                                // Add code for the next variables
                                double volts = jsonresponse.getDouble("volts");
                                double amps = jsonresponse.getDouble("amps");
                                double power = jsonresponse.getDouble("power");
                                double energy = jsonresponse.getDouble("energy");
                                double cost = jsonresponse.getDouble("cost");

                                // Update States
                                voltsBar.setProgress((int) volts);
                                voltsText.setText(volts + "V");
                                ampsBar.setProgress((int) amps);
                                ampsText.setText(amps + " A");
                                powerUsed.setText(power + " Wh");
                                unitsConsumed.setText(energy + " Watts");
                                amountGenerated.setText(cost + " Rs.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                };

                //Initialize request string with POST method
                StringRequest request = new StringRequest(Request.Method.POST, url, listener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("id", Integer.toString(relayID));
                        return param;
                    }
                };
                Volleyton.getInstance(getApplicationContext()).addToRequestQueue(request);

                // Publish the progress
                publishProgress();

                // Sleep for 1 second (so that the loop doesn't run repeatedly and use CPU resources
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (aSyncCancelled) {
                    break;
                }

            }
            return null;
        }

        /**
         * Function that will be called after every cycle to update the dataset
         */
        @Override
        protected void onProgressUpdate(String... values) {
            //tvTimePassed.setText(newTime);
        }
    }
}
