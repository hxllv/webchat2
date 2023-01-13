package com.example.webchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
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

public class FriendsListAdapter extends BaseAdapter {
    Context context;
    Activity ac;
    GlobalVars app;
    List<JSONObject> friends;
    LayoutInflater inflter;

    public FriendsListAdapter(Activity ac, GlobalVars app, Context applicationContext, List<JSONObject> friends) {
        this.context = applicationContext;
        this.ac = ac;
        this.app = app;
        this.friends = friends;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return friends.size();
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
        view = inflter.inflate(R.layout.activity_friend_list_view, null);
        TextView name = view.findViewById(R.id.textView);
        Button unfriend = view.findViewById(R.id.unfriend);
        Button chat = view.findViewById(R.id.chat);

        unfriend.setOnClickListener(view1 -> {
            try {
                app.delete2(friends.get(i).getString("id"));

                ac.finish();
                ac.startActivity(ac.getIntent());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        chat.setOnClickListener(view1 -> {
            try {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("chatId", friends.get(i).getString("id"));
                ac.startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        try {
            name.setText(friends.get(i).getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}