package com.okunev.apiexample.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.okunev.apiexample.PreferencesHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Project ApiExample. Created by okunev on 3/4/17.
 */

public class HelperAPI {
    private static final String TAG = HelperAPI.class.getSimpleName();
    private static final String VOLX_TOKEN_HEADER = "Volx-Token";
    private static final String VOLX_DEVICE_HEADER = "Volx-Device";
    private static final String PROD_SERVER = "api.volx.ru/mobile/api/v16";
    private static final String DEV_SERVER = "api.dev.volx.ru/mobile/api/v16";
    private static final String SEND_SMS_PATH = "/users/send-register-sms";
    private static final String MY_ORDERS_PATH = "/users/orders";
    private static final String CART_ADD_PATH = "/cart/add";
    private static boolean isDevServer = true;
    private OnResultGotListener listener;
    private String token;
    private String deviceId;
    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            silentlyFail(e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (listener != null) listener.OnResultGot(true, response.body().string());
        }
    };

    public HelperAPI(Context context) {
        this.token = PreferencesHelper.getUserToken(context);
        this.deviceId = PreferencesHelper.getDeviceId(context);
    }

    private static String getServerStatic() {
        return isDevServer ? DEV_SERVER : PROD_SERVER;
    }

    public static void backgroundThreadShortToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
        }
    }

    public void setListener(OnResultGotListener listener) {
        this.listener = listener;
    }

    public void sendSms() {
        try {
            post(SEND_SMS_PATH, getUserHeaders(), null);
        } catch (Exception e) {
            silentlyFail(e);
        }
    }

    public void cartAdd(Object item) {
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("item", new Gson().toJson(item));
            post(CART_ADD_PATH, getUserHeaders(), parameters);
        } catch (Exception e) {
            silentlyFail(e);
        }
    }

    public void getMyOrders(String filter, Integer page) {
        try {
            if (filter == null || filter.isEmpty())
                get(MY_ORDERS_PATH, getUserHeaders(), "?page=" + page + "&perPage=40");
            else
                get(MY_ORDERS_PATH, getUserHeaders(), "?filter=" + filter + "&page=" + page + "&perPage=40");
        } catch (Exception e) {
            silentlyFail(e);
        }
    }

    public void getOrderInfo(String orderNumber) {
        try {
            get(MY_ORDERS_PATH, getUserHeaders(), "/" + orderNumber);
        } catch (Exception e) {
            silentlyFail(e);
        }
    }

    private HashMap<String, String> getUserHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(VOLX_TOKEN_HEADER, token);
        headers.put(VOLX_DEVICE_HEADER, deviceId);
        return headers;
    }

    private void silentlyFail(Exception e) {
        Log.d(TAG, e.getMessage());
        if (listener != null)
            listener.OnResultGot(false, e.getMessage());
    }


    /**
     * Query a URL for their source code. Post request
     *
     * @param url        The page's URL.
     * @param headers    Headers map
     * @param parameters Parameters as JSON
     *                   return The body.
     */
    private void post(String url, @Nullable Map<String, String> headers, @Nullable Map<String, String> parameters) throws IOException {
        OkHttpClient client = Api.getClient();
        Request.Builder builder = new Request.Builder();
        builder.url("http://" + getServerStatic() + url);
        if (headers != null) {
            Set<String> headersSet = headers.keySet();
            for (String key : headersSet)
                builder.addHeader(key, headers.get(key));
        }
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (parameters != null) {
            Set<String> params = parameters.keySet();
            for (String parameter : params) {
                bodyBuilder.add(parameter, parameters.get(parameter));
            }
        }
        builder.post(bodyBuilder.build());
        Request request = builder.build();
        client.newCall(request).enqueue(callback);
    }

    private void get(String url, @Nullable Map<String, String> headers, String parameters) throws IOException {
        OkHttpClient client = Api.getClient();
        Request.Builder builder = new Request.Builder();
        builder.url("http://" + getServerStatic() + url + parameters);
        if (headers != null) {
            Set<String> headersSet = headers.keySet();
            for (String key : headersSet)
                builder.addHeader(key, headers.get(key));
        }
        Request request = builder.build();
        client.newCall(request).enqueue(callback);
    }

    public interface OnResultGotListener {
        void OnResultGot(Boolean success, String response);
    }
}
