package com.example.webchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SearchAdapter extends BaseAdapter {
    Context context;
    Activity ac;
    GlobalVars app;
    List<JSONObject> searchs;
    LayoutInflater inflter;

    public SearchAdapter(Activity ac, GlobalVars app, Context applicationContext, List<JSONObject> searchs) {
        this.context = applicationContext;
        this.ac = ac;
        this.app = app;
        this.searchs = searchs;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return searchs.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_search_list_view, null);
        TextView name = view.findViewById(R.id.textView);
        Button accept = view.findViewById(R.id.accept);
        Button decline = view.findViewById(R.id.decline);
        Button chat = view.findViewById(R.id.chat);
        Button unfriend = view.findViewById(R.id.unfriend);
        Button send = view.findViewById(R.id.send);
        Button cancel = view.findViewById(R.id.cancel);

        try {
            name.setText(searchs.get(i).getString("username"));

            if (searchs.get(i).getString("isFriends").equals("true")) {
                chat.setVisibility(View.VISIBLE);
                unfriend.setVisibility(View.VISIBLE);

                chat.setOnClickListener(view1 -> {
                    try {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("chatId", searchs.get(i).getString("id"));
                        ac.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

                unfriend.setOnClickListener(view1 -> {
                    try {
                        app.delete2(searchs.get(i).getString("id"));

                        ac.finish();
                        ac.startActivity(ac.getIntent());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
            else if (searchs.get(i).getString("isRequested4Me").equals("true")) {
                accept.setVisibility(View.VISIBLE);
                decline.setVisibility(View.VISIBLE);

                accept.setOnClickListener(view1 -> {
                    try {
                        app.update2(searchs.get(i).getString("id"));

                        ac.finish();
                        ac.startActivity(ac.getIntent());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

                decline.setOnClickListener(view1 -> {
                    try {
                        app.delete2(searchs.get(i).getString("id"));

                        ac.finish();
                        ac.startActivity(ac.getIntent());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
            else if (searchs.get(i).getString("isRequested4U").equals("true")) {
                cancel.setVisibility(View.VISIBLE);

                cancel.setOnClickListener(view1 -> {
                    try {
                        app.delete2(searchs.get(i).getString("id"));

                        ac.finish();
                        ac.startActivity(ac.getIntent());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
            else {
                send.setVisibility(View.VISIBLE);

                send.setOnClickListener(view1 -> {
                    try {
                        app.store(searchs.get(i).getString("id"));

                        ac.finish();
                        ac.startActivity(ac.getIntent());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
