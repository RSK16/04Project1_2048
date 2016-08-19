package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cskaoyan.zhao.a04mobilemanager.R;
import com.cskaoyan.zhao.a04mobilemanager.bean.Contact;

import java.util.ArrayList;

public class ContactsListActivity extends AppCompatActivity {


    private static final String TAG = "ContactsListActivity";
    ArrayList<Contact> contactlist;
    private ListView lv_contactlistactivity_contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        getSupportActionBar().hide();

        contactlist = new ArrayList<Contact>();

        //contactlist 从contentprovide 去访问系统联系人数据库，解析数据库

        //耗时操作不要在主线程执行
        new Thread(new Runnable() {
            @Override
            public void run() {
                getContactListFromProvider();

                //myHandler.sendEmptyMessage(0);

                // 如果不想直接自己去写handler接收并处理消息的话，简便写法（底层还是通过消息机制处理）
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv_contactlistactivity_contacts.setAdapter(new MyContactsAdapter());
                    }
                });
            }
        }).start();

        lv_contactlistactivity_contacts = (ListView) findViewById(R.id.lv_contactlistactivity_contacts);

        lv_contactlistactivity_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent result = new Intent();
                String phonenumber = contactlist.get(position).getPhonenumber();
                Log.i(TAG,"phonenumber="+phonenumber);
                result.putExtra("number",phonenumber);

                setResult(200,result);

                finish();


            }
        });
    }



    /*Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                lv_contactlistactivity_contacts.setAdapter(new MyContactsAdapter());
            }

            super.handleMessage(msg);
        }
    };*/

    private void getContactListFromProvider() {

        //去解析系统联系人的数据库

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor1 = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                new String[]{"contact_id"}, null, null, null);

        while (cursor1.moveToNext()){

            int contact_id = cursor1.getInt(0);

            Contact contact = new Contact();
            Cursor cursor2 = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                    new String[]{"data1","mimetype"}, "raw_contact_id=?", new String[]{contact_id+""}, null);


            while(cursor2.moveToNext()){

                String   type= cursor2.getString(1);

                if (type.equals("vnd.android.cursor.item/name")){
                    String   name= cursor2.getString(0);
                    contact.setName(name);
                }else if(type.equals("vnd.android.cursor.item/phone_v2")){
                    String   number= cursor2.getString(0);
                    contact.setPhonenumber(number);
                }

            }
            contactlist.add(contact);
            contact=null;
        }

        Log.i(TAG,contactlist.toString());

    }

    class MyContactsAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return contactlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View inflate = View.inflate(ContactsListActivity.this, R.layout.item_contactlist, null);

            TextView tv_contactlist_name = (TextView) inflate.findViewById(R.id.tv_contactlist_name);
            TextView tv_contactlist_phonmenumber = (TextView) inflate.findViewById(R.id.tv_contactlist_phonmenumber);

            Contact contact = contactlist.get(position);

            tv_contactlist_name.setText(contact.getName());
            tv_contactlist_phonmenumber.setText(contact.getPhonenumber());

            return inflate;
        }
    }

}
