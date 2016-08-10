package com.tongchuang.perimetrypro.user.impl;

import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongchuang.perimetrypro.network.VisionRestClient;
import com.tongchuang.perimetrypro.user.UserInfo;
import com.tongchuang.perimetrypro.user.UserService;
import com.tongchuang.perimetrypro.user.UserServiceResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by qingdi on 8/5/16.
 */
public class UserServiceImpl implements UserService {

    private Gson gson = new Gson();

    @Override
    public void getUserInfo(String userId, final UserServiceResponseHandler responseHandler) {

        String url = "/users/"+userId+"?apiKey=rock2016";
        // trigger the network request
        VisionRestClient.get(url, null, new JsonHttpResponseHandler() {
            // define the success and failure callbacks
            // handle the successful response (popular photos JSON)
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                System.out.println("statusCode = " + statusCode);
                System.out.println("res = " + res);
                t.printStackTrace();
                responseHandler.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                System.out.println("response.length="+response.length());
                UserInfo userInfo = gson.fromJson(response.toString(), UserInfo.class);
                responseHandler.onSuccess(userInfo);
            }
        });
    }
}
