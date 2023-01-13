package com.example.webchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RequestAdapter extends BaseAdapter {
    Context context;
    Activity ac;
    GlobalVars app;
    List<JSONObject> requests;
    LayoutInflater inflter;

    public RequestAdapter(Activity ac, GlobalVars app, Context applicationContext, List<JSONObject> requests) {
        this.context = applicationContext;
        this.ac = ac;
        this.app = app;
        this.requests = requests;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return requests.size();
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
        view = inflter.inflate(R.layout.activity_request_list_view, null);
        TextView name = view.findViewById(R.id.textView);
        Button accept = view.findViewById(R.id.accept);
        Button decline = view.findViewById(R.id.decline);

        accept.setOnClickListener(view1 -> {
            try {
                app.update2(requests.get(i).getString("id"));

                ac.finish();
                ac.startActivity(ac.getIntent());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        decline.setOnClickListener(view1 -> {
            try {
                app.delete2(requests.get(i).getString("id"));

                ac.finish();
                ac.startActivity(ac.getIntent());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        try {
            name.setText(requests.get(i).getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
