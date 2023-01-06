package com.example.webchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
                    String bodyS = "{\"email\":\"" + inputEmail+ "\", \"password\":\"" + inputPass + "\"}";
                    JSONObject body = null;
                    try {
                        body = new JSONObject(bodyS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:8000/api/login", body,
                            response -> {
                                try {
                                    ((GlobalVars) getApplication()).setApiToken(response.getString("token"));
                                    startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                            },
                            error -> {
                                    Log.e("neki", error.toString());
//                                if (error.networkResponse.statusCode == 503)
//                                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
//                                else
                                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                    );

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            }
        });
    }
}