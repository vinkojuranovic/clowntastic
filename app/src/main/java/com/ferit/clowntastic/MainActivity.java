package com.ferit.clowntastic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ferit.clowntastic.adapters.ClownsAdapter;
import com.ferit.clowntastic.adapters.OrdersAdapter;
import com.ferit.clowntastic.helpers.DatabaseHelper;
import com.ferit.clowntastic.models.Order;
import com.ferit.clowntastic.models.Type;
import com.ferit.clowntastic.models.User;
import com.ferit.clowntastic.utilis.API;
import com.ferit.clowntastic.utilis.ApplicationConstants;

import java.util.List;

import static android.R.attr.type;

public class MainActivity extends AppCompatActivity {

    private API apiService;
    private DatabaseHelper databaseHelper;
    private TextView tvInstruction;
    private ListView mainListView;
    private String userType;
    private Button btSeeOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = DatabaseHelper.getInstance(this);
        apiService = API.getInstance(this);

        SharedPreferences sharedPreferences = getSharedPreferences(ApplicationConstants.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        userType = sharedPreferences.getString(ApplicationConstants.KEY_TYPE, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userType.equals(Type.CLOWN.toString())) {
            setUpUIClown();
        } else {
            setUpUICustomer();
        }
    }

    private void setUpUICustomer() {
        mainListView = (ListView) findViewById(R.id.lv_mainListView);
        tvInstruction = (TextView) findViewById(R.id.tv_instruction);
        btSeeOrders = (Button) findViewById(R.id.bt_seeOrders);
        btSeeOrders.setVisibility(View.VISIBLE);

        List<User> clowns = databaseHelper.getAllUsers();
        tvInstruction.setText(getResources().getString(R.string.instruction));

        ClownsAdapter clownsAdapter = new ClownsAdapter(clowns);
        mainListView.setAdapter(clownsAdapter);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent makeOrderIntent = new Intent(MainActivity.this, MakeOrder.class);
                makeOrderIntent.putExtra(ApplicationConstants.KEY_ID, id);
                startActivity(makeOrderIntent);
            }
        });
        btSeeOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckOrders.class);
                startActivity(intent);
            }
        });
    }

    private void setUpUIClown() {
        mainListView = (ListView) findViewById(R.id.lv_mainListView);
        mainListView = (ListView) findViewById(R.id.lv_mainListView);
        tvInstruction = (TextView) findViewById(R.id.tv_instruction);

        final List<Order> orders = databaseHelper.getAllOrders();
        tvInstruction.setText(getResources().getString(R.string.instruction_clown));

        OrdersAdapter ordersAdapter = new OrdersAdapter(orders, this);
        mainListView.setAdapter(ordersAdapter);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent makeOrderIntent = new Intent(MainActivity.this, ConfirmOrder.class);
                Log.e("SEND ID", String.valueOf(id));
                makeOrderIntent.putExtra(ApplicationConstants.KEY_ID, id);
                startActivity(makeOrderIntent);
            }
        });
    }

    public void developerTools(View view) {
        Intent intent = new Intent(this, AndroidDatabaseManager.class);
        startActivity(intent);
    }

}
