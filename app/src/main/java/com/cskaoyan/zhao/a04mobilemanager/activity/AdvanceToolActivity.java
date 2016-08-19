package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cskaoyan.zhao.a04mobilemanager.R;

public class AdvanceToolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_tool);

        getSupportActionBar().hide();
    }

    public void jump(View v){

        startActivity(new Intent(this,QueryLoctionActivity.class));
    }
}
