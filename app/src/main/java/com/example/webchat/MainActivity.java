package com.example.webchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText eEmail;
    private EditText ePassword;
    private Button eLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eEmail = findViewById(R.id.editTextTextEmailAddress);
        ePassword = findViewById(R.id.editTextTextPassword);
        eLogin = findViewById(R.id.buttonLogin);

        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputEmail = eEmail.getText().toString();
                String inputPass = ePassword.getText().toString();

                if(inputEmail.isEmpty() || inputPass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Email or password field is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    String url = "http://10.0.2.2:8000/api/login";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    ((GlobalVars) getApplication()).setApiToken(response);
                                    startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                                    //Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error.networkResponse.statusCode == 503)
                                        Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("email", inputEmail);
                            params.put("password", inputPass);

                            return params;
                        }
                    };

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            }
        });
    }
}