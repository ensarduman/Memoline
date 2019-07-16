package com.ensarduman.memoline.DTO;


import android.provider.ContactsContract;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by duman on 08/03/2018.
 */

public class ResponseDTO<T> {
    T Data;
    int StatusCode;
    String StatusText;

    public ResponseDTO() {
    }

    public ResponseDTO(String json, Class<T> typeParameterClass) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            Gson gson = new Gson();
            String strData = jsonObject.getString("Data");

            if(strData != null) {
                this.Data = (T) gson.fromJson(strData, typeParameterClass);
            }
            else
            {
                this.Data = null;
            }

            this.StatusCode = jsonObject.getInt("StatusCode");
            this.StatusText = jsonObject.getString("StatusText");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public String getStatusText() {
        return StatusText;
    }

    public void setStatusText(String statusText) {
        StatusText = statusText;
    }
}
