package com.okunev.apiexample;

import android.app.Application;

import com.okunev.apiexample.network.Api;

/**
 * Project ApiExample. Created by gwa on 3/4/17.
 */

public class ApiExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Api.init();
    }
}
