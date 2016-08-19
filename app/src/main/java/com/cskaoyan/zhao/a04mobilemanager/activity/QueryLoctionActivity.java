package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.dao.MyLocationQueryDao;

public class QueryLoctionActivity extends AppCompatActivity {

    private EditText et_queryloctaionactvity_number;
    private TextView tv_qeurylocationactivity_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_loction);

        getSupportActionBar().hide();
        et_queryloctaionactvity_number = (EditText) findViewById(R.id.et_queryloctaionactvity_number);

        tv_qeurylocationactivity_location = (TextView) findViewById(R.id.tv_qeurylocationactivity_location);
    }


    //离线版的号码归属地 ？
    //139 5566 4567
    public void  query(View v){

        String number = et_queryloctaionactvity_number.getText().toString();
        String location = MyLocationQueryDao.queryLocation(this, number);
        tv_qeurylocationactivity_location.setText(location);
    }

}
