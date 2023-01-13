package com.example.webchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    ListView simpleList;
    List<JSONObject> msgs = new ArrayList<>();
    EditText msg;
    Button send;
    ChatAdapter chatAdapter;
    int maxScroll = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msg = findViewById(R.id.msg);
        send = findViewById(R.id.send);

        String chatId = getIntent().getStringExtra("chatId");

        RequestQueue queue = Volley.newRequestQueue(ChatActivity.this);

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, "http://10.0.2.2:8000/api/chat/" + chatId + "?not-view=true", null,
                response -> {
                    Log.e("isithere", response.toString());
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject object;
                        try {
                            object = response.getJSONObject(i);
                            msgs.add(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        msgs.add(new JSONObject().put("message", "SHOULD NOT SEE THIS").put("sender_id", chatId));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    simpleList = findViewById(R.id.chatListView);
                    chatAdapter = new ChatAdapter(ChatActivity.this, (GlobalVars) getApplication(), getApplicationContext(), msgs, chatId);
                    simpleList.setAdapter(chatAdapter);

                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            updateMsgs(queue, chatId);
                            handler.postDelayed(this, 3000L);
                        }
                    };
                    handler.post(runnable);
                },
                error -> {
                    Toast.makeText(ChatActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

        send.setOnClickListener(view -> {
            sendMsg(msg.getText().toString(), queue, chatId);
        });
    }

    private void updateMsgs(RequestQueue queue, String chatId) {
        int preCount = chatAdapter.getCount();

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, "http://10.0.2.2:8000/api/chat/" + chatId + "?not-view=true", null,
            response -> {
                Log.e("isithere", response.toString());

                chatAdapter.msgs.clear();

                for(int i=0;i<response.length();i++)
                {
                    JSONObject object;
                    try {
                        object = response.getJSONObject(i);
                        chatAdapter.msgs.add(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    chatAdapter.msgs.add(new JSONObject().put("message", "SHOULD NOT SEE THIS").put("sender_id", chatId));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                chatAdapter.notifyDataSetChanged();

                int postCount = chatAdapter.getCount();

                if (postCount != preCount || maxScroll == 0) {
                    View c = simpleList.getChildAt(0);
                    maxScroll = chatAdapter.getCount() * c.getHeight();
                }

                Log.i("maxscroll", String.valueOf(maxScroll));

                View c = simpleList.getChildAt(0);
                int scrolly = -c.getTop() + simpleList.getLastVisiblePosition() * c.getHeight();

                Log.i("scrolly", String.valueOf(scrolly));

                if (maxScroll - scrolly < 400) {
                    simpleList.setSelection(chatAdapter.getCount());
                }
            },
            error -> {
                Log.e("err", error.toString());
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

    private void sendMsg(String mesg, RequestQueue queue, String chatId) {
        String bodyS = "{\"message\":\"" + mesg+ "\", \"messageType\":\"0\"}";
        JSONObject body = null;
        try {
            body = new JSONObject(bodyS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:8000/api/chat/" + chatId, body,
            response -> {
                msg.setText("");
                Log.e("isithere", response.toString());
                updateMsgs(queue, chatId);
            },
            error -> {
                Log.e("err", error.toString());
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