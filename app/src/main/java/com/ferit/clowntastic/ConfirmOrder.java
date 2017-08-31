package com.ferit.clowntastic;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ferit.clowntastic.helpers.DatabaseHelper;
import com.ferit.clowntastic.interfaces.RequestListener;
import com.ferit.clowntastic.models.Order;
import com.ferit.clowntastic.models.User;
import com.ferit.clowntastic.utilis.API;
import com.ferit.clowntastic.utilis.ApplicationConstants;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ConfirmOrder extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private DatabaseHelper databaseHelper;
    private Order order;
    private User customer;
    private API apiService;
    private GoogleMap maps;
    private TextView tvCustomerName, tvOrderDate, tvOrderAddress, tvPackage;
    private Button btOrderAccept, btOrderRefuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        databaseHelper = DatabaseHelper.getInstance(this);
        Intent intent = getIntent();

        order = databaseHelper.getOrderById(intent.getLongExtra(ApplicationConstants.KEY_ID, 0));
        customer = databaseHelper.getUserByServerId(order.getCustomerId());

        apiService = API.getInstance(this);
        setUpUI();
    }

    private void setUpUI() {
        tvCustomerName = (TextView) findViewById(R.id.tv_customerName);
        tvOrderDate = (TextView) findViewById(R.id.tv_orderDate);
        tvOrderAddress = (TextView) findViewById(R.id.tv_orderAddress);
        tvPackage = (TextView) findViewById(R.id.tv_package);
        btOrderAccept = (Button) findViewById(R.id.bt_orderAccept);
        btOrderRefuse = (Button) findViewById(R.id.bt_orderRefuse);

        tvCustomerName.setText(customer.getFirstName() + " " + customer.getLastName());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Integer.valueOf(order.getDate()) * 1000);
        tvOrderDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "." +
                calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR) + ".");
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(order.getLatitude(), order.getLongtitude(), 1);
                Address address = addressList.get(0);
                String addressString = address.getAddressLine(0) + " " + address.getLocality() +
                        " " + address.getCountryName();

                tvOrderAddress.setText(addressString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tvPackage.setText(order.getaPackage().toString());
        btOrderAccept.setOnClickListener(this);
        btOrderRefuse.setOnClickListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.fMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View v) {
        Boolean orderStatus = Boolean.FALSE;
        switch (v.getId()) {
            case R.id.bt_orderAccept:
                orderStatus = Boolean.TRUE;
                break;
            case R.id.bt_orderRefuse:
                orderStatus = Boolean.FALSE;
                break;
        }
        order.setStatus(orderStatus);
        apiService.updateOrder(order, new RequestListener() {
            @Override
            public void failed(String message) {
                Toast.makeText(ConfirmOrder.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void finished(String message) {
                databaseHelper.updateOrderStatus(order);
                ConfirmOrder.this.finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;

        UiSettings uiSettings = maps.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        maps.setMyLocationEnabled(true);
        maps.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {
        maps.addMarker(new MarkerOptions()
                .position(new LatLng(order.getLatitude(), order.getLongtitude()))
                .title("Party!!"));
    }
}
