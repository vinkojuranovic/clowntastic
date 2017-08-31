package com.ferit.clowntastic.helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequestQueue {

    private RequestQueue requestQueue;
    private static VolleyRequestQueue volleyRequestQueue;
    private static  Context context;

    private VolleyRequestQueue(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestQueue getInstance(Context context) {
        if (volleyRequestQueue == null) {
            volleyRequestQueue = new VolleyRequestQueue(context);
        }
        return volleyRequestQueue;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

}
