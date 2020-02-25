package com.example.switchme;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogsActivity extends AppCompatActivity {

    private boolean aSyncCancelled;

    // Recycler view variables
    private RecyclerView               recyclerView;
    private LogsAdapter                adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context                    context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        // Add back-able button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Binding variables to layout
        recyclerView = findViewById(R.id.list_main);

        // Initializing values
        aSyncCancelled = false;
        context = getApplicationContext();

        // Setting up recycler view
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LogsAdapter(new ArrayList<LogsModel>());
        recyclerView.setAdapter(adapter);

        // Get the first list of logs
        String url = MainActivity.URL + "/getLogs.php";
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonresponse = new JSONObject(response.toString());
                    boolean success = jsonresponse.getBoolean("successful");
                    JSONArray jsonDataset = jsonresponse.getJSONArray("logs");

                    for (int i = 0; i < jsonDataset.length(); i++) {
                        JSONObject singleLog = jsonDataset.getJSONObject(i);
                        String rid = singleLog.getString("roomID");
                        String energy = singleLog.getString("energyUsed");
                        String start = singleLog.getString("startedAt");
                        String end = singleLog.getString("endedAt");

                        adapter.addToDataset(new LogsModel(rid, start, end, energy));
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


    }
}
