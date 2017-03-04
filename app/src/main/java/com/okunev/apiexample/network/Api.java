package com.okunev.apiexample.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Project ApiExample. Created by okunev on 3/4/17.
 */

public class Api {
    private static OkHttpClient okHttpClient;

    public static void init() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    public static OkHttpClient getClient() {
        return okHttpClient;
    }
}
