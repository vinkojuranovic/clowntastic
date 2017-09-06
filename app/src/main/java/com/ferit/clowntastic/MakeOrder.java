package com.ferit.clowntastic;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ferit.clowntastic.helpers.DatabaseHelper;
import com.ferit.clowntastic.interfaces.RequestListener;
import com.ferit.clowntastic.models.Order;
import com.ferit.clowntastic.models.Package;
import com.ferit.clowntastic.models.Type;
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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MakeOrder extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private API apiService;
    private GoogleMap maps;
    private DatabaseHelper databaseHelper;
    private User clown;
    private LatLng latLng;
    private Package selectedPackage;
    private Button btSetDate, btOrder;
    private Calendar selectedTime;
    private TextView tvDate, tvClownName, tvAddress;
    private Spinner spPackages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);

        databaseHelper = DatabaseHelper.getInstance(this);
        Intent intent = getIntent();

        clown = databaseHelper.getUserById(intent.getLongExtra(ApplicationConstants.KEY_ID, 0));

        apiService = API.getInstance(this);
        setUpUI();
    }

    private void setUpUI() {
        btSetDate = (Button) findViewById(R.id.bt_setDate);
        btOrder = (Button) findViewById(R.id.bt_order);
        tvDate = (TextView) findViewById(R.id.tv_Date);
        spPackages = (Spinner) findViewById(R.id.sp_package);
        tvClownName = (TextView) findViewById(R.id.tv_clownName);
        tvAddress = (TextView) findViewById(R.id.tv_address);

        tvClownName.setText(clown.getName());
        ArrayAdapter<Package> packageArrayAdapter = new ArrayAdapter<Package>(this, android.R.layout.simple_spinner_item, Package.values());
        spPackages.setAdapter(packageArrayAdapter);
        spPackages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPackage = Package.valueOf((int) id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btSetDate.setOnClickListener(this);
        btOrder.setOnClickListener(this);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.fMap);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_setDate:
                final Dialog dateTimeDialog = new Dialog(this);
                dateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dateTimeDialog.setContentView(R.layout.time_date_picker);
//                dateTimeDialog.setTitle("Select date and time!");

                Calendar calendar = Calendar.getInstance();
                Integer year = calendar.get(Calendar.YEAR);
                Integer month = calendar.get(Calendar.MONTH) + 1;
                Integer dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                final DatePicker datePicker = (DatePicker) dateTimeDialog.findViewById(R.id.dp_date);
                final TimePicker timePicker = (TimePicker) dateTimeDialog.findViewById(R.id.tp_time);
                Button confirmDateTime = (Button) dateTimeDialog.findViewById(R.id.bt_confirmDate);

                timePicker.setIs24HourView(true);
                datePicker.init(year, month, dayOfMonth, null);
                confirmDateTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer hour, minute;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hour = timePicker.getHour();
                            minute = timePicker.getMinute();
                        } else {
                            hour = timePicker.getCurrentHour();
                            minute = timePicker.getCurrentMinute();
                        }
                        selectedTime = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(),
                                datePicker.getDayOfMonth(), hour, minute);
                        String dateTimeString = String.valueOf(datePicker.getDayOfMonth()) + "." +
                                datePicker.getMonth() + "." +
                                datePicker.getYear() + ". " +
                                hour + ":" + minute;
                        tvDate.setText(dateTimeString);
                        dateTimeDialog.dismiss();

                    }
                });
                dateTimeDialog.show();
                break;
            case R.id.bt_order:
                String date = String.valueOf(tvDate.getText());
                if (date.isEmpty()) {
                    Toast.makeText(this, getResources().getString(R.string.error_missing, "Date"), Toast.LENGTH_SHORT).show();
                } else if (selectedPackage == null) {
                    Toast.makeText(this, getResources().getString(R.string.error_missing, "Package"), Toast.LENGTH_SHORT).show();
                } else if (latLng == null) {
                    Toast.makeText(this, getResources().getString(R.string.error_missing, "Address"), Toast.LENGTH_SHORT).show();
                } else {
                    final Order order = new Order(
                            String.valueOf(selectedTime.getTimeInMillis() / 1000),
                            latLng.latitude,
                            latLng.longitude,
                            clown.getServerId(),
                            0L,
                            selectedPackage,
                            false
                    );
                    apiService.createOrder(order, new RequestListener() {
                        @Override
                        public void failed(String message) {
                            Toast.makeText(MakeOrder.this, message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void finished(String id) {
                            order.setServerId(Long.valueOf(id));
                            databaseHelper.createOrder(order);
                            MakeOrder.this.finish();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;

        UiSettings uiSettings = maps.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        maps.setOnMapClickListener(this);
        maps.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        this.latLng = latLng;
        maps.clear();
        maps.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marked")
                .snippet("You have marked this!`"));
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                Address address = addressList.get(0);
                StringBuilder addressString = new StringBuilder();
                addressString.append(address.getAddressLine(0)).append(" ").append(address.getLocality())
                        .append(" ").append(address.getCountryName());

                tvAddress.setText(addressString.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
