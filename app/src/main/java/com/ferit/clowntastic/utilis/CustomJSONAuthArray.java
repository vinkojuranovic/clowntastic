package com.ferit.clowntastic.utilis;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CustomJSONAuthArray extends JsonArrayRequest {

    private Context context;

    public CustomJSONAuthArray(int method, String url, JSONArray jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener, Context context) {
        super(method, url, jsonRequest, listener, errorListener);
        this.context = context;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> params = new HashMap<String, String>();
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            String jwt = sharedPreferences.getString(ApplicationConstants.KEY_JWT, "");
            String applicationAuthorization = "Bearer " + jwt;
            params.put(ApplicationConstants.APPLICATION_AUTHORIZATION, applicationAuthorization);
        }
        return params;
    }

}
