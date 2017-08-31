package com.ferit.clowntastic.utilis;

public final class ApplicationConstants {

    public static final String SHARED_PREFERENCES_NAME = "clownData";
    public static final String API_BASE_URL = "https://clowntastic.herokuapp.com/api";

    /**
     * API KEYS
     */
    public static final String APPLICATION_AUTHORIZATION = "Authorization";
    public static final String KEY_JWT = "jwt";
    public static final String KEY_TYPE = "type";
    public static final String KEY_STATUS = "status";

    /**
     * API ENDPOINTS
     */
    public static final String ENDPOINT_LOGIN = "/login";
    public static final String ENDPOINT_REGISTER = "/register";
    public static final String ENDPOINT_USERS = "/users";
    public static final String ENDPOINT_ORDERS = "/orders";
    public static final String ENDPOINT_ORDER = "/order";

    /**
     * Login request
     */
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    /**
     * Register request
     */
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";

    /**
     * Users request
     */
    public static final String KEY_ID = "id";

    /**
     * Orders requests
     */
    public static final String KEY_DATE = "date";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_PACKAGE = "package";
    public static final String KEY_CLOWN_ID = "clownId";
    public static final String KEY_CUSTOMER_ID = "customerId";
}
