package com.example.webchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText eEmail;
    private EditText ePassword;
    private Button eLogin;
    private Button eRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.token), "");

        Log.e("work", token);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        if (!token.equals("")) {
            Log.e("work", "in 1");
            JsonObjectRequest stringRequest1 = new JsonObjectRequest(Request.Method.GET, "http://10.0.2.2:8000/api/test", null,
                    response -> {
                        Log.e("work", "in 2");
                        try {
                            Log.e("work", response.getString("success"));
                            if (response.getString("success").equals("true")) {
                                ((GlobalVars) getApplication()).setApiToken(token);
                                startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.e("work", error.toString());
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };
            queue.add(stringRequest1);
        }

        eEmail = findViewById(R.id.editTextTextEmailAddress);
        ePassword = findViewById(R.id.editTextTextPassword);
        eLogin = findViewById(R.id.buttonLogin);
        eRegister = findViewById(R.id.register);

        eRegister.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://10.0.2.2:8000/register"));
            startActivity(browserIntent);
        });

        eLogin.setOnClickListener(view -> {
            String inputEmail = eEmail.getText().toString();
            String inputPass = ePassword.getText().toString();
            eLogin.setEnabled(false);

            if(inputEmail.isEmpty() || inputPass.isEmpty()) {
                Toast.makeText(MainActivity.this, "Email or password field is empty", Toast.LENGTH_SHORT).show();
                eLogin.setEnabled(true);
            }
            else {
                String bodyS = "{\"email\":\"" + inputEmail+ "\", \"password\":\"" + inputPass + "\"}";
                JSONObject body = null;
                try {
                    body = new JSONObject(bodyS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:8000/api/login", body,
                    response -> {
                        try {
                            ((GlobalVars) getApplication()).setApiToken(response.getString("token"));

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.token), response.getString("token"));
                            editor.apply();

                            startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        eLogin.setEnabled(true);
                        //Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                            Log.e("neki", error.toString());
//                                if (error.networkResponse.statusCode == 503)
//                                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
//                                else
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        eLogin.setEnabled(true);
                    }
                );

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
    }
}