package com.example.webchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

public class SearchActivity extends AppCompatActivity {

    private EditText eInput;
    private Button eSearch;
    ListView simpleList;
    //List<JSONObject> searchs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        eInput = findViewById(R.id.editTextTextPersonName);
        eSearch = findViewById(R.id.searchButton);

        simpleList = findViewById(R.id.searchListView);
        SearchAdapter searchAdapter = new SearchAdapter(SearchActivity.this, (GlobalVars) getApplication(), getApplicationContext(), new ArrayList<>());
        simpleList.setAdapter(searchAdapter);

        RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);

        eSearch.setOnClickListener(view -> {
            String input = eInput.getText().toString();

            JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, "http://10.0.2.2:8000/api/search?query=" + input, null,
                response -> {
                    searchAdapter.searchs.clear();

                    Log.e("isithere", response.toString());
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject object;
                        try {
                            object = response.getJSONObject(i);
                            searchAdapter.searchs.add(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    searchAdapter.notifyDataSetChanged();
                },
                error -> {
                    Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
        });
    }
}