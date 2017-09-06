package com.ferit.clowntastic.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ferit.clowntastic.models.Order;
import com.ferit.clowntastic.models.Package;
import com.ferit.clowntastic.models.Type;
import com.ferit.clowntastic.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final Integer DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "clowntastic";

    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /**
     * Tables
     */
    private static final String TABLE_USER = "users";
    private static final String TABLE_ORDER = "orders";

    /**
     * Column names
     */
    //users
    private static final String KEY_ID = "id";
    private static final String KEY_SERVER_ID = "server_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_TYPE = "type";

    //orders
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_CLOWN_ID = "clownId";
    private static final String KEY_CUSTOMER_ID = "customerId";
    private static final String KEY_PACKAGE = "package";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATE = "date";

    /**
     * Table creation queries
     */
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " (" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_SERVER_ID + " INTEGER," +
            KEY_EMAIL + " TEXT," +
            KEY_TYPE + " TEXT," +
            KEY_FIRST_NAME + " TEXT," +
            KEY_LAST_NAME + " TEXT)";

    private static final String CREATE_TABLE_ORDER = "CREATE TABLE " + TABLE_ORDER + " (" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_SERVER_ID + " INTEGER," +
            KEY_DATE + " TEXT," +
            KEY_LATITUDE + " TEXT," +
            KEY_LONGITUDE + " TEXT," +
            KEY_CLOWN_ID + " LONG," +
            KEY_CUSTOMER_ID + " LONG," +
            KEY_PACKAGE + " TEXT," +
            KEY_STATUS + " INTEGER)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_ORDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        onCreate(db);
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                userList.add(new User(
                        Long.valueOf(cursor.getString(0)),
                        Long.valueOf(cursor.getString(1)),
                        cursor.getString(2),
                        Type.valueOf(cursor.getString(3).toUpperCase()),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return userList;
    }

    public User getUserById(Long id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_USER, null, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        User user = new User(
                Long.valueOf(cursor.getString(0)),
                Long.valueOf(cursor.getString(1)),
                cursor.getString(2),
                Type.valueOf(cursor.getString(3).toUpperCase()),
                cursor.getString(4),
                cursor.getString(5)
        );
        sqLiteDatabase.close();
        cursor.close();
        return user;
    }

    public User getUserByServerId(Long id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_USER, null, KEY_SERVER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        User user = new User(
                Long.valueOf(cursor.getString(0)),
                Long.valueOf(cursor.getString(1)),
                cursor.getString(2),
                Type.valueOf(cursor.getString(3).toUpperCase()),
                cursor.getString(4),
                cursor.getString(5)
        );
        sqLiteDatabase.close();
        cursor.close();
        return user;
    }

    public void addUser(User user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_ID, user.getServerId());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_TYPE, user.getType().toString());
        values.put(KEY_FIRST_NAME, user.getFirstName());
        values.put(KEY_LAST_NAME, user.getLastName());

        sqLiteDatabase.insert(TABLE_USER, null, values);
        sqLiteDatabase.close();
    }

    public Order getOrderById(Long id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_ORDER, null, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        Order order = new Order(
                Long.valueOf(cursor.getString(0)),
                Long.valueOf(cursor.getString(1)),
                cursor.getString(2),
                Double.valueOf(cursor.getString(3)),
                Double.valueOf(cursor.getString(4)),
                Long.valueOf(cursor.getString(5)),
                Long.valueOf(cursor.getString(6)),
                Package.valueOf(cursor.getString(7).toUpperCase()),
                Integer.valueOf(cursor.getString(8)) == 1
        );
        sqLiteDatabase.close();
        cursor.close();
        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> ordersList = new ArrayList<Order>();
        String selectQuery = "SELECT  * FROM " + TABLE_ORDER;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ordersList.add(new Order(
                        Long.valueOf(cursor.getString(0)),
                        Long.valueOf(cursor.getString(1)),
                        cursor.getString(2),
                        Double.valueOf(cursor.getString(3)),
                        Double.valueOf(cursor.getString(4)),
                        Long.valueOf(cursor.getString(5)),
                        Long.valueOf(cursor.getString(6)),
                        Package.valueOf(cursor.getString(7).toUpperCase()),
                        Integer.valueOf(cursor.getString(8)) != 0
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return ordersList;
    }

    public void createOrder(Order order) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_ID, order.getServerId());
        values.put(KEY_DATE, order.getDate());
        values.put(KEY_LONGITUDE, String.valueOf(order.getLongtitude()));
        values.put(KEY_LATITUDE, String.valueOf(order.getLatitude()));
        values.put(KEY_CLOWN_ID, String.valueOf(order.getClownId()));
        values.put(KEY_CUSTOMER_ID, String.valueOf(order.getCustomerId()));
        values.put(KEY_PACKAGE, order.getaPackage().name());
        values.put(KEY_STATUS, order.getStatus() ? 1 : 0);

        sqLiteDatabase.insert(TABLE_ORDER, null, values);
        sqLiteDatabase.close();
    }

    public void updateOrderStatus(Order order) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, order.getStatus());

        sqLiteDatabase.update(TABLE_ORDER, values, KEY_ID + " = ?",
                new String[]{String.valueOf(order.getId())});
        sqLiteDatabase.close();
    }

    public void clearDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_ORDER);
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_USER);
        sqLiteDatabase.close();
    }
}
