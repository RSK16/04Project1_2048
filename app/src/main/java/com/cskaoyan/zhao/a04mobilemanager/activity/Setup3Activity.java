package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;

public class Setup3Activity extends SetupBaseAcitivty {

    private static final String TAG ="Setup3Activity" ;
    private EditText et_setup3activity_safenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        et_setup3activity_safenum = (EditText) findViewById(R.id.et_setup3activity_safenum);

        String safenum = MyApplication.getStringFromSp("safenum");
        et_setup3activity_safenum.setText(safenum);

    }



    //实现如下功能：跳到一个页面上，让用户去选择一个本机的联系人。
    //这个联系人显示的页面 需要实现（ListView+contentProvider）。
    //两个页面之间传递数据
    public void selectContact(View v){


       // startActivityForResult(new Intent(this,ContactsListActivity.class),100);

        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

        startActivityForResult(intent,200);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Log.i(TAG,requestCode+"+"+resultCode);
        if (requestCode==100&&resultCode==200){


            String number = data.getStringExtra("number");
            Log.i(TAG,"number="+number);
            et_setup3activity_safenum.setText(number);

        }
        else if (requestCode==200){

            Uri uri = data.getData();

            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);

            cursor.moveToNext();
            String string = cursor.getString(0);
            et_setup3activity_safenum.setText(string);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void next(View v){

        String safenum = et_setup3activity_safenum.getText().toString();

        if (safenum.isEmpty()){

            Toast.makeText(Setup3Activity.this, "安全号码不能为空", Toast.LENGTH_SHORT).show();
        }else{
            //保存安全号码
            MyApplication.saveStringToSp("safenum",safenum);

            startActivity(new Intent(this,Setup4Activity.class));
            overridePendingTransition(R.anim.slidein_rigth,R.anim.slideout_left);

        }


    }

    public void previous(View v){
        startActivity(new Intent(this,Setup2Activity.class));
        overridePendingTransition(R.anim.slidein_left,R.anim.sildeout_rigth );

    }
}
