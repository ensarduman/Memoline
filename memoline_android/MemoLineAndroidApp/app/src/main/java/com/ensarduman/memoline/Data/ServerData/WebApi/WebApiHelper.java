package com.ensarduman.memoline.Data.ServerData.WebApi;

import android.content.Context;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ensarduman on 10.03.2018.
 */

class WebApiHelper {
    Context context;
    String accessKey;
    //String urlBase = "http://memolineserver.ensarduman.com/api/";
    String urlBase = "http://memolineservertest.ensarduman.com/api/";
    //String urlBase = "http://192.168.1.8/api/";

    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public WebApiHelper(Context context, String accessKey) {
        this.context = context;
        this.accessKey = accessKey;
    }

    public String GetRequest(String url){
        String rv = null;

        Request.Builder reqBuilder = new Request.Builder()
                .url(urlBase + url);

        Request request;

        if(accessKey != null) {
            request = reqBuilder
                    .addHeader("access_key", accessKey)
                    .build();
        }
        else
        {
            request = reqBuilder
                    .build();
        }

        Response response = null;

        try {
            response = client.newCall(request).execute();
            rv =  response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rv;
    }

    public String PostRequest(String url, String jsonData)
    {
        String rv = null;

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request.Builder reqBuilder = new Request.Builder()
                .url(urlBase + url);
        Request request;

        if(accessKey != null) {
            request = reqBuilder
                    .addHeader("access_key", accessKey)
                    .post(body)
                    .build();
        }
        else
        {
            request = reqBuilder
                    .post(body)
                    .build();
        }

        Response response = null;
        try {
            response = client.newCall(request).execute();
            rv = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rv;
    }

}
