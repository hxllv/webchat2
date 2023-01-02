package com.example.webchat;

import android.app.Application;

public class GlobalVars extends Application {

    private String apiToken;

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
