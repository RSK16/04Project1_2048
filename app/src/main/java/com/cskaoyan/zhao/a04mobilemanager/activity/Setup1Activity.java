package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cskaoyan.zhao.a04mobilemanager.R;

public class Setup1Activity extends SetupBaseAcitivty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

        getSupportActionBar().hide();

        /*Button b=null;
        b.setOnClickListener();*/
    }

    @Override
    public void previous(View v) {

    }


    public void next(View v){

        startActivity(new Intent(this,Setup2Activity.class));
        overridePendingTransition(R.anim.slidein_rigth,R.anim.slideout_left);

    }
}
