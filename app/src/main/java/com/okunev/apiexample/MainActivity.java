package com.okunev.apiexample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.okunev.apiexample.network.HelperAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HelperAPI api = new HelperAPI(this);
        api.setListener((success, response) -> {
            if (success) {
                if (response != null && !response.isEmpty()) {
                    Orders orders = new Gson().fromJson(response, Orders.class);
                    //Do something with orders
                } else {
                    HelperAPI.backgroundThreadShortToast(MainActivity.this, "Error!");
                }
            } else {
                HelperAPI.backgroundThreadShortToast(MainActivity.this, "Error!");
            }
        });
        api.getMyOrders("iPhone", 0);

        //А может быть такое, что сервер возвращает их не сразу.
        api.setListener((success, response) -> {
            if (success) {
                Handler mainHandler = new Handler(getMainLooper());
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    if (object.getString("status").equals("ok")) {
                        Orders orders = new Gson().fromJson(response, Orders.class);
                        //Do something with orders
                    } else if (object.getString("status").equals("pending")) {
                        HelperAPI.backgroundThreadShortToast(MainActivity.this, "Processing request!");
                        mainHandler.postDelayed(() -> api.getMyOrders("iPhone", 0), 2000);
                    } else if (object.getString("status").equals("error")) {
                        HelperAPI.backgroundThreadShortToast(MainActivity.this, "Cannot receive your orders!");
                    }
                } catch (JSONException ignored) {
                    HelperAPI.backgroundThreadShortToast(MainActivity.this, "Error!");
                }
            } else {
                HelperAPI.backgroundThreadShortToast(MainActivity.this, "Error!");
            }
        });
        api.getMyOrders("iPhone", 0);
    }
}
