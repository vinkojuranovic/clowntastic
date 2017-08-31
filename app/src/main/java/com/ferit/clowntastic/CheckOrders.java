package com.ferit.clowntastic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ferit.clowntastic.adapters.CheckOrdersAdapter;
import com.ferit.clowntastic.helpers.DatabaseHelper;
import com.ferit.clowntastic.models.Order;

import java.util.List;

public class CheckOrders extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView lvOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_orders);

        databaseHelper = DatabaseHelper.getInstance(this);

        setUpUI();
    }

    private void setUpUI() {
        lvOrders = (ListView) findViewById(R.id.lv_ordersList);
        List<Order> orders = databaseHelper.getAllOrders();
        CheckOrdersAdapter checkOrdersAdapter = new CheckOrdersAdapter(orders, this);
        lvOrders.setAdapter(checkOrdersAdapter);

    }

}
