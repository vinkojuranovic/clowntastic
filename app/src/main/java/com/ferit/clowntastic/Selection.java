package com.ferit.clowntastic;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ferit.clowntastic.helpers.DatabaseHelper;
import com.ferit.clowntastic.models.Type;
import com.ferit.clowntastic.utilis.API;
import com.ferit.clowntastic.utilis.ApplicationConstants;

public class Selection extends AppCompatActivity implements View.OnClickListener {

    private Button btClownSelection, btCustomerSelection;
    private ImageView ivBlueBalloon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setUpUI();
        DatabaseHelper.getInstance(this).clearDatabase();
    }

    private void setUpUI() {
        btClownSelection = (Button) findViewById(R.id.bt_clownSelection);
        btCustomerSelection = (Button) findViewById(R.id.bt_customerSelection);

        btClownSelection.setOnClickListener(this);
        btCustomerSelection.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Login.class);
        switch (v.getId()) {
            case R.id.bt_clownSelection:
                intent.putExtra(ApplicationConstants.KEY_TYPE, Type.CLOWN.toString());
                break;
            case R.id.bt_customerSelection:
                intent.putExtra(ApplicationConstants.KEY_TYPE, Type.CUSTOMER.toString());
                break;
        }
        startActivity(intent);
        finish();
    }


}
