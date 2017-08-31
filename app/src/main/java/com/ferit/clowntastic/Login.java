package com.ferit.clowntastic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ferit.clowntastic.interfaces.RequestListener;
import com.ferit.clowntastic.models.Type;
import com.ferit.clowntastic.utilis.API;
import com.ferit.clowntastic.utilis.ApplicationConstants;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Toolbar myToolbar;
    private Button btLogIn, btRegister;
    private EditText etEmail, etPassword;
    private API apiService;
    private LinearLayout llProgressStatus;
    private String typeString;
    private TextView tvCandC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        apiService = API.getInstance(this);
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        typeString = extra.getString(ApplicationConstants.KEY_TYPE);
        if (typeString != null) {
            typeString = typeString.toLowerCase();
        }

        setUpUI();
    }

    private void setUpUI() {
        btLogIn = (Button) findViewById(R.id.bt_login);
        btRegister = (Button) findViewById(R.id.bt_register);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        llProgressStatus = (LinearLayout) findViewById(R.id.ll_progressStatus);
        tvCandC = (TextView) findViewById(R.id.tv_CandC);

        if(typeString.equals(Type.CLOWN.toString())) {
            tvCandC.setText(Type.CLOWN.toString());
            tvCandC.setTextColor(getResources().getColor(R.color.red));
        } else {
            tvCandC.setText(Type.CUSTOMER.toString());
            tvCandC.setTextColor(getResources().getColor(R.color.blue));
        }
        btRegister.setOnClickListener(this);
        btLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                btLogIn.setVisibility(View.GONE);
                llProgressStatus.setVisibility(View.VISIBLE);
                String email = String.valueOf(etEmail.getText());
                String password = String.valueOf(etPassword.getText());
                if (email.isEmpty()) {
                    etEmail.setError(getResources().getString(R.string.error_missing, "Email"));
                    btLogIn.setVisibility(View.VISIBLE);
                    llProgressStatus.setVisibility(View.GONE);
                } else if (password.isEmpty()) {
                    etPassword.setError(getResources().getString(R.string.error_missing, "Passoword"));
                    btLogIn.setVisibility(View.VISIBLE);
                    llProgressStatus.setVisibility(View.GONE);
                } else {
                    apiService.login(email, password, new RequestListener() {
                        @Override
                        public void failed(String message) {
                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            btLogIn.setVisibility(View.VISIBLE);
                            llProgressStatus.setVisibility(View.GONE);
                        }

                        @Override
                        public void finished(String type) {
                            if (typeString != null && typeString.equals(type.toLowerCase())) {
                                getUsers();
                            } else {
                                Toast.makeText(Login.this, getResources().getString(R.string.error_role_match), Toast.LENGTH_SHORT).show();
                                btLogIn.setVisibility(View.VISIBLE);
                                llProgressStatus.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                break;
            case R.id.bt_register:
                Intent registerIntent = new Intent(this, Register.class);
                startActivity(registerIntent);
                break;
        }
    }


    private void getUsers() {
        apiService.users(new RequestListener() {
            @Override
            public void failed(String message) {
                btLogIn.setVisibility(View.VISIBLE);
                llProgressStatus.setVisibility(View.GONE);
                Toast.makeText(Login.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void finished(String message) {
                getOrders();
            }
        });
    }

    private void getOrders() {
        apiService.orders(new RequestListener() {
            @Override
            public void failed(String message) {
                btLogIn.setVisibility(View.VISIBLE);
                llProgressStatus.setVisibility(View.GONE);
                Toast.makeText(Login.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void finished(String message) {
                Intent mainIntent = new Intent(Login.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mainIntent);
                finish();
            }
        });
    }

}
