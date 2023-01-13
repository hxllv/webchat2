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

public class ChatAdapter extends BaseAdapter {
    Context context;
    Activity ac;
    GlobalVars app;
    List<JSONObject> msgs;
    LayoutInflater inflter;
    String chatId;

    public ChatAdapter(Activity ac, GlobalVars app, Context applicationContext, List<JSONObject> msgs, String chatId) {
        this.context = applicationContext;
        this.ac = ac;
        this.app = app;
        this.msgs = msgs;
        this.chatId = chatId;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return msgs.size() - 1;
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
        view = inflter.inflate(R.layout.activity_chat_list_view, null);
        TextView me = view.findViewById(R.id.me);
        TextView him = view.findViewById(R.id.him);
        View meView = view.findViewById(R.id.meView);

        try {
            if(msgs.get(i).getString("sender_id").equals(chatId)) {
                him.setVisibility(View.VISIBLE);
                him.setText(msgs.get(i).getString("message"));
            }
            else {
                me.setVisibility(View.VISIBLE);
                meView.setVisibility(View.VISIBLE);
                me.setText(msgs.get(i).getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
