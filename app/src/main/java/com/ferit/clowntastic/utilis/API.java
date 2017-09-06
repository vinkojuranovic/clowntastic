package com.ferit.clowntastic.utilis;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ferit.clowntastic.R;
import com.ferit.clowntastic.helpers.DatabaseHelper;
import com.ferit.clowntastic.helpers.VolleyRequestQueue;
import com.ferit.clowntastic.interfaces.RequestListener;
import com.ferit.clowntastic.models.Order;
import com.ferit.clowntastic.models.Package;
import com.ferit.clowntastic.models.Type;
import com.ferit.clowntastic.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class API {

    private static API APIService;
    private Activity activity;
    private VolleyRequestQueue volleyRequestQueue;

    public static synchronized API getInstance(Activity activity) {
        if (APIService == null) {
            APIService = new API(activity);
        }
        return APIService;
    }

    private API(Activity activity) {
        this.activity = activity;
    }

    public void login(final String email, final String password, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApplicationConstants.KEY_EMAIL, email);
            jsonObject.put(ApplicationConstants.KEY_PASSWORD, password);
        } catch (JSONException e) {
            requestListener.failed(e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                ApplicationConstants.API_BASE_URL + ApplicationConstants.ENDPOINT_LOGIN, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("Request LOG", String.valueOf(response));
                    String jwt = response.getString(ApplicationConstants.KEY_JWT);
                    String type = response.getString(ApplicationConstants.KEY_TYPE);
                    if (jwt != null) {
                        SharedPreferences sharedPreferences = activity.getSharedPreferences(ApplicationConstants.SHARED_PREFERENCES_NAME,
                                Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(ApplicationConstants.KEY_JWT, jwt);
                        editor.putString(ApplicationConstants.KEY_TYPE, type);
                        editor.commit();
                        requestListener.finished(type);
                    } else {
                        requestListener.failed("Login failed");
                    }
                } catch (Exception e) {
                    Log.e("ERROR LOG", String.valueOf(e));
                    requestListener.failed(activity.getString(R.string.volley_request_fail));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error == null) {
                    requestListener.failed(activity.getString(R.string.volley_request_fail));
                }
            }
        });

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    public void register(final String email, final String password, String fName, String lName, Type type, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApplicationConstants.KEY_EMAIL, email);
            jsonObject.put(ApplicationConstants.KEY_PASSWORD, password);
            jsonObject.put(ApplicationConstants.KEY_FIRST_NAME, fName);
            jsonObject.put(ApplicationConstants.KEY_LAST_NAME, lName);
            jsonObject.put(ApplicationConstants.KEY_TYPE, type.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                ApplicationConstants.API_BASE_URL + ApplicationConstants.ENDPOINT_REGISTER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Integer status = response.getInt(ApplicationConstants.KEY_STATUS);
                    if (status == 0) {
                        requestListener.failed("Email is taken");
                    } else {
                        requestListener.finished("success");
                    }
                } catch (Exception e) {
                    requestListener.failed(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.failed("Server error please try again");
            }
        });

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    public void users(final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);
        final DatabaseHelper databaseHelper = DatabaseHelper.getInstance(activity);

        CustomJSONAuthArray jsonAuthArray = new CustomJSONAuthArray(Request.Method.GET,
                ApplicationConstants.API_BASE_URL + ApplicationConstants.ENDPOINT_USERS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.e("Request LOG", String.valueOf(response));
                    Integer usersLength = response.length();
                    if (usersLength != 0) {
                        for (int i = 0; i < usersLength; ++i) {
                            JSONObject userObject = (JSONObject) response.get(i);
                            databaseHelper.addUser(new User(
                                    userObject.getLong(ApplicationConstants.KEY_ID),
                                    userObject.getString(ApplicationConstants.KEY_EMAIL),
                                    Type.valueOf(userObject.getString(ApplicationConstants.KEY_TYPE).toUpperCase()),
                                    userObject.getString(ApplicationConstants.KEY_FIRST_NAME),
                                    userObject.getString(ApplicationConstants.KEY_LAST_NAME)
                                    ));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR SYNCUS", e.getMessage());
                    requestListener.failed(e.getMessage());
                }
                requestListener.finished("Users updated");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    requestListener.failed(error.getMessage());
                    Log.e("ERROR SYNCUS2", error.getMessage());
                } else {
                    Log.e("ERROR SYNCUS3", "FAILED");
                    requestListener.failed("Request failed");
                }
            }
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonAuthArray);
    }

    public void createOrder(Order order, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApplicationConstants.KEY_DATE, order.getDate());
            jsonObject.put(ApplicationConstants.KEY_LATITUDE, order.getLatitude());
            jsonObject.put(ApplicationConstants.KEY_LONGITUDE, order.getLongtitude());
            jsonObject.put(ApplicationConstants.KEY_PACKAGE, order.getaPackage().getId());
            jsonObject.put(ApplicationConstants.KEY_CLOWN_ID, order.getClownId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONAuthObject jsonObjectRequest = new CustomJSONAuthObject(Request.Method.POST,
                ApplicationConstants.API_BASE_URL + ApplicationConstants.ENDPOINT_ORDER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESPONSE", String.valueOf(response));
                try {
                    Integer status = response.getInt(ApplicationConstants.KEY_STATUS);
                    if (status == 0) {
                        requestListener.failed("Order creation failed");
                    } else {
                        Long orderId = response.getLong(ApplicationConstants.KEY_ID);
                        requestListener.finished(String.valueOf(orderId));
                    }
                } catch (Exception e) {
                    requestListener.failed(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR CROR2", error.getMessage());
                requestListener.failed(error.getMessage());
            }
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }

    public void orders(final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);
        final DatabaseHelper databaseHelper = DatabaseHelper.getInstance(activity);

        CustomJSONAuthArray jsonAuthArray = new CustomJSONAuthArray(Request.Method.GET,
                ApplicationConstants.API_BASE_URL + ApplicationConstants.ENDPOINT_ORDERS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.e("Request LOG", String.valueOf(response));
                    Integer ordersLength = response.length();
                    if (ordersLength != 0) {
                        for (int i = 0; i < ordersLength; ++i) {
                            JSONObject orderObject = (JSONObject) response.get(i);
                            databaseHelper.createOrder(new Order(
                                    orderObject.getLong(ApplicationConstants.KEY_ID),
                                    orderObject.getString(ApplicationConstants.KEY_DATE),
                                    orderObject.getDouble(ApplicationConstants.KEY_LATITUDE),
                                    orderObject.getDouble(ApplicationConstants.KEY_LONGITUDE),
                                    orderObject.getLong("clown_id"),
                                    orderObject.getLong("customer_id"),
                                    Package.valueOf(orderObject.getInt(ApplicationConstants.KEY_PACKAGE)),
                                    orderObject.getInt(ApplicationConstants.KEY_STATUS) == 1
                            ));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR SYNCUS", e.getMessage());
                    requestListener.failed(e.getMessage());
                }
                requestListener.finished("Orders updated");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    requestListener.failed(error.getMessage());
                    Log.e("ERROR SYNCUS2", error.getMessage());
                } else {
                    Log.e("ERROR SYNCUS3", "FAILED");
                    requestListener.failed("Request failed");
                }
            }
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonAuthArray);
    }

    public void updateOrder(Order order, final RequestListener requestListener) {
        volleyRequestQueue = VolleyRequestQueue.getInstance(activity);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApplicationConstants.KEY_ID, order.getServerId());
            jsonObject.put(ApplicationConstants.KEY_STATUS, order.getStatus());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONAuthObject jsonObjectRequest = new CustomJSONAuthObject(Request.Method.PUT,
                ApplicationConstants.API_BASE_URL + ApplicationConstants.ENDPOINT_ORDER, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESPONSE", String.valueOf(response));
                try {
                    Integer status = response.getInt(ApplicationConstants.KEY_STATUS);
                    if (status == 0) {
                        requestListener.failed("Order update failed");
                    } else {
                        requestListener.finished("Update successful");
                    }
                } catch (Exception e) {
                    requestListener.failed(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.failed(error.getMessage());
            }
        }, activity);

        volleyRequestQueue.addToRequestQueue(jsonObjectRequest);
    }
}
