package com.example.webchat;

import android.app.Application;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class GlobalVars extends Application {

    private String apiToken;

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String store(String id) {
        AtomicReference<String> returnVal = new AtomicReference<>();

        RequestQueue queue = Volley.newRequestQueue(GlobalVars.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://10.0.2.2:8000/api/friend/" + id,
                returnVal::set,
            error -> {
                Log.e("store", error.toString());
            }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getApiToken());
                return headers;
            }
        };

        queue.add(stringRequest);

        return returnVal.toString();
    }

    public String update2(String id) {
        AtomicReference<String> returnVal = new AtomicReference<>();

        RequestQueue queue = Volley.newRequestQueue(GlobalVars.this);

        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, "http://10.0.2.2:8000/api/friend/" + id,
                returnVal::set,
                error -> {
                    Log.e("store", error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getApiToken());
                return headers;
            }
        };

        queue.add(stringRequest);

        return returnVal.toString();
    }

    public String delete2(String id) {
        AtomicReference<String> returnVal = new AtomicReference<>();

        RequestQueue queue = Volley.newRequestQueue(GlobalVars.this);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, "http://10.0.2.2:8000/api/friend/" + id,
                response -> {
                    returnVal.set(response);
                    Log.e("store", response);
                },
                error -> {
                    Log.e("store", error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getApiToken());
                return headers;
            }
        };

        queue.add(stringRequest);

        return returnVal.toString();
    }
}
