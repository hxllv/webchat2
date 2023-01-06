package com.example.webchat;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<JSONObject> friends;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, List<JSONObject> friends) {
        this.context = applicationContext;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_friend_list_view, null);
        TextView country = view.findViewById(R.id.textView);
        try {
            country.setText(friends.get(i).getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}