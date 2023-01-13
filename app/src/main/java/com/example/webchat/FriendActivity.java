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

public class FriendActivity extends AppCompatActivity {

    ListView simpleList;
    List<JSONObject> friends = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        Log.e("token", ((GlobalVars) getApplication()).getApiToken());
        RequestQueue queue = Volley.newRequestQueue(FriendActivity.this);

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, "http://10.0.2.2:8000/api/friends", null,
            response -> {
                Log.e("isithere", response.toString());
                for(int i=0;i<response.length();i++)
                {
                    JSONObject object;
                    try {
                        object = response.getJSONObject(i);
                        friends.add(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                simpleList = findViewById(R.id.friendListView);
                FriendsListAdapter friendsListAdapter = new FriendsListAdapter(FriendActivity.this, (GlobalVars) getApplication(), getApplicationContext(), friends);
                simpleList.setAdapter(friendsListAdapter);
            },
            error -> {
                Toast.makeText(FriendActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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