package com.cskaoyan.zhao.a04mobilemanager.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cskaoyan.zhao.a04mobilemanager.R;

/**
 * Created by zhao on 2016/8/5.
 */
public class SettingItem extends RelativeLayout {
    private static final String TAG = "SettingItem";
    private Context ctx;
    private TextView tv_setting_description;
    private TextView tv_setting_title;
    private String settingonString;
    private String settingoffString;
    private String settingSpKey;
    private String settingtitle ;
    private CheckBox cb_settting_checkbox;

    private MySettingItemOnCheckStateChangeListener onCheckStateChangeListener;
    private SharedPreferences config;

    public SettingItem(Context context) {
        super(context);
        ctx =context;
        init(null);
    }


    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx =context;
        init(attrs);
    }


    public interface  MySettingItemOnCheckStateChangeListener{

        void onChecked();
        void onUnChecked();

    }

    public void SetMySettingItemOnCheckStateChangeListener(MySettingItemOnCheckStateChangeListener l){

        onCheckStateChangeListener =l;
    }

    private void init( AttributeSet attrs) {

        config = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);

        //settingtitle

        if (attrs!=null){
            settingtitle = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "settingtitle");
            settingonString = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "settingonString");
            settingoffString = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "settingoffString");
            settingSpKey = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "settingSpKey");
        }

        View inflate = View.inflate(ctx, R.layout.item_setting, null);
        addView(inflate);

        tv_setting_title = (TextView) inflate.findViewById(R.id.tv_setting_title);
        cb_settting_checkbox = (CheckBox) inflate.findViewById(R.id.cb_settting_checkbox);
        tv_setting_description = (TextView) inflate.findViewById(R.id.tv_setting_description);

        //根据sp里保存的用户设置，去在页面初始化的时候 回显用户设置
        boolean state = config.getBoolean(settingSpKey, false);
        cb_settting_checkbox.setChecked(state);
        tv_setting_description.setText(state?settingonString:settingoffString);

        //根据自定义控件里的自定义属性去给自定义控件设置初始值
        tv_setting_title.setText(settingtitle);


        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG,"onclick");

                boolean checked = cb_settting_checkbox.isChecked();

                SharedPreferences.Editor edit = config.edit();
                edit.putBoolean(settingSpKey,!checked);
                edit.commit();

                if (checked){
                    cb_settting_checkbox.setChecked(false);//
                    tv_setting_description.setText(settingoffString);

                    //解绑sim卡,如果用户没有设置不会调用callback，有设置，才会调用
                    if (onCheckStateChangeListener!=null)
                        onCheckStateChangeListener.onUnChecked();
                }else{
                    cb_settting_checkbox.setChecked(true);//
                    tv_setting_description.setText(settingonString);

                    //绑定sim卡
                    //如果用户没有设置，不会调用callback，有设置，才会调用
                    if (onCheckStateChangeListener!=null)
                        onCheckStateChangeListener.onChecked();
                }

            }
        });
    }


    public void setCheck(boolean check){
        cb_settting_checkbox.setChecked(check);
        tv_setting_description.setText(check?settingonString:settingoffString);
        SharedPreferences.Editor edit = config.edit();
        edit.putBoolean(settingSpKey,check);
        edit.commit();
    }
}
