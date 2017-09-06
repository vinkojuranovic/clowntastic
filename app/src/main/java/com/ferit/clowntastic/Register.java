package com.ferit.clowntastic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ferit.clowntastic.interfaces.RequestListener;
import com.ferit.clowntastic.models.Type;
import com.ferit.clowntastic.utilis.API;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private API apiService;
    private EditText etEmail, etPassword, etPasswordConfirm, etFirstName, etLastName;
    private Button btRegister;
    private Spinner spType;
    private Type selectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        apiService = API.getInstance(this);

        setUpUI();
    }

    private void setUpUI() {
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPasswordConfirm = (EditText) findViewById(R.id.et_password_confirm);
        etFirstName = (EditText) findViewById(R.id.et_firstName);
        etLastName = (EditText) findViewById(R.id.et_lastName);
        btRegister = (Button) findViewById(R.id.bt_register);
        spType = (Spinner) findViewById(R.id.sp_type);

        ArrayAdapter<Type> arrayAdapter = new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_item, Type.values());
        spType.setAdapter(arrayAdapter);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ID ITEM", String.valueOf(id));
                Log.e("ID ITEM", String.valueOf(position));
                selectedType = Type.valueOf((int) id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_register) {
            String email = String.valueOf(etEmail.getText());
            String firstName = String.valueOf(etFirstName.getText());
            String lastName = String.valueOf(etLastName.getText());
            String password = String.valueOf(etPassword.getText());
            String passwordConfirm = String.valueOf(etPasswordConfirm.getText());
            if (email.isEmpty()) {
                etEmail.setError(getResources().getString(R.string.error_missing, "Email"));
            } else if (password.isEmpty()) {
                etPassword.setError(getResources().getString(R.string.error_missing, "First name"));
            } else if (firstName.isEmpty()) {
                etFirstName.setError(getResources().getString(R.string.error_missing, "Password"));
            } else if (lastName.isEmpty()) {
                etLastName.setError(getResources().getString(R.string.error_missing, "Last name"));
            } else if (passwordConfirm.isEmpty()) {
                etPasswordConfirm.setError(getResources().getString(R.string.error_missing, "Password confirmation"));
            } else if (!password.equals(passwordConfirm)) {
                etPassword.setError(getResources().getString(R.string.error_password));
                etPasswordConfirm.setError(getResources().getString(R.string.error_password));
            } else {
                btRegister.setVisibility(View.GONE);
                API registerService = API.getInstance(this);

                registerService.register(email, password, firstName, lastName, selectedType, new RequestListener() {
                    @Override
                    public void failed(String message) {
                        Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                        btRegister.setVisibility(View.VISIBLE);
                        Log.e("FAILED", message);
                    }

                    @Override
                    public void finished(String message) {
                        Register.this.finish();
                    }
                });
            }
        }
    }
}
