package com.example.switchme;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    private Context      context;
    private ToggleButton tbSwitch;
    private TextView     tvTimePassed;
    private ListUpdater  updateList;
    private Calendar     calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tbSwitch = findViewById(R.id.switcher_tb);
        tvTimePassed = findViewById(R.id.time_passed_tv);

        context = this;

        // Execute Thread
        // Thread to keep updating the list
        updateList = new ListUpdater();
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
                        param.put("state", finalNewCheckedState);
                        param.put("time", "" + t.get(Calendar.YEAR) + "-" + t.get(Calendar.MONTH) + "-" + t.get(Calendar.DATE) + " " + t.get(Calendar.HOUR) + ":" + t.get(Calendar.MINUTE) + ":" + t.get(Calendar.SECOND));
                        return param;
                    }
                };
                Volleyton.getInstance(getApplicationContext()).addToRequestQueue(request);
            }
        });
    }

    @Override
    protected void onDestroy() {
        updateList.cancel(true);
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
                            } else {
                                switchTB(true);
                                long diff = Calendar.getInstance().getTime().getTime() - Math.abs(updated.getTime());
                                updateCalendar(TimeUnit.MILLISECONDS.toSeconds(diff));
                                String message =
                                        "" + (calendar.get(Calendar.HOUR) < 10 ? "0" + calendar.get(Calendar.HOUR) :
                                        calendar.get(Calendar.HOUR)) + " Hours | " + calendar.get(Calendar.MINUTE) + " Minutes | " +
                                        calendar.get(Calendar.SECOND) + " Seconds";
                                updateTimePassed(message);
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
                        return param;
                    }
                };
                Volleyton.getInstance(getApplicationContext()).addToRequestQueue(request);

                // Publish the progress
                publishProgress();

                // Sleep for 1 second (so that the loop doesn't run repeatedly and use CPU resources
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
