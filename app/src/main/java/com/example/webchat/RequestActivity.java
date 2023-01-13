package com.example.webchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {

    ListView simpleList;
    List<JSONObject> requests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Log.e("token", ((GlobalVars) getApplication()).getApiToken());
        RequestQueue queue = Volley.newRequestQueue(RequestActivity.this);

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, "http://10.0.2.2:8000/api/requests", null,
                response -> {
                    Log.e("isithere", response.toString());
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject object;
                        try {
                            object = response.getJSONObject(i);
                            requests.add(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    simpleList = findViewById(R.id.requestListView);
                    RequestAdapter requestAdapter = new RequestAdapter(RequestActivity.this, (GlobalVars) getApplication(), getApplicationContext(), requests);
                    simpleList.setAdapter(requestAdapter);
                },
                error -> {
                    Toast.makeText(RequestActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + ((GlobalVars) getApplication()).getApiToken());
                return headers;
            }
        };

        queue.add(stringRequest);
    }
}